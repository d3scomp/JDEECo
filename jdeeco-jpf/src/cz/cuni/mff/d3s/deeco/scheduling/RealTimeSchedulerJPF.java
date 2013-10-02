package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.executor.TaskExecutor;
import cz.cuni.mff.d3s.deeco.executor.ThreadPoolTaskExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.IKnowledgeChangeListener;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerType;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.Trigger;
import cz.cuni.mff.d3s.deeco.path.grammar.PathGrammar;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.provider.PeriodicTaskProvider;
import cz.cuni.mff.d3s.deeco.task.provider.TriggeredComponentProcessTaskProvider;
import cz.cuni.mff.d3s.deeco.task.provider.TriggeredEnsembleTaskProvider;
import cz.cuni.mff.d3s.deeco.task.provider.TriggeredTaskProvider;

public class RealTimeSchedulerJPF extends Scheduler {

	private final Map<PeriodicTaskProvider, Timer> periodicTaskTimers;
	private final Map<TriggeredTaskProvider, TriggeredTaskListener> triggeredTaskListeners;
	private final TaskExecutor executor;

	private final KnowledgeManager km;

	private boolean isRunning;

	public RealTimeSchedulerJPF(TaskExecutor executor, KnowledgeManager km) {
		this.executor = executor;
		this.periodicTaskTimers = new HashMap<>();
		this.triggeredTaskListeners = new HashMap<>();
		this.km = km;
		this.isRunning = false;
	}
	
	public RealTimeSchedulerJPF(KnowledgeManager km) {
		this(new ThreadPoolTaskExecutor(km), km);
	}

	@Override
	public long getCurrentTime() {
		return System.currentTimeMillis();
	}

	@Override
	public synchronized void start() {
		if (!isRunning) {
			for (PeriodicTaskProvider taskProvider : periodicTaskTimers
					.keySet()) {
				Timer timer = new Timer();
				timer.scheduleAtFixedRate(new PeriodicTimerTask(taskProvider),
						0, taskProvider.getPeriod());
				periodicTaskTimers.put(taskProvider, timer);
			}
			km.setListenersActive(true);
			isRunning = true;
		}

	}

	@Override
	public synchronized void stop() {
		if (isRunning) {
			for (Timer timer : periodicTaskTimers.values())
				timer.cancel();
			km.setListenersActive(false);
			isRunning = false;
		}

	}

	@Override
	public synchronized boolean isStarted() {
		return isRunning;
	}

	@Override
	public synchronized void add(PeriodicTaskProvider taskProvider) {
		if (!periodicTaskTimers.containsKey(taskProvider)) {
			Timer timer = null;
			if (isRunning) {
				timer = new Timer();
				timer.scheduleAtFixedRate(new PeriodicTimerTask(taskProvider),
						0, taskProvider.getPeriod());
			}
			periodicTaskTimers.put(taskProvider, timer);
		}
	}

	@Override
	public synchronized void add(TriggeredTaskProvider taskProvider) {
		if (!triggeredTaskListeners.containsKey(taskProvider)) {
			TriggeredTaskListener taskListener = new TriggeredTaskListener(
					taskProvider);
			triggeredTaskListeners.put(taskProvider, taskListener);
			km.registerListener(taskListener);
		}
	}

	@Override
	public synchronized void remove(PeriodicTaskProvider taskProvider) {
		// TODO Auto-generated method stub
	}

	@Override
	public synchronized void remove(TriggeredTaskProvider taskProvider) {
		// TODO Auto-generated method stub
	}

	protected synchronized void execute(Task task) {
		executor.submitTask(task);
	}

	private class PeriodicTimerTask extends TimerTask {

		private final PeriodicTaskProvider taskProvider;

		public PeriodicTimerTask(PeriodicTaskProvider taskProvider) {
			this.taskProvider = taskProvider;
		}

		@Override
		public void run() {
			execute(taskProvider.getTask());
		}

	}

	private class TriggeredTaskListener implements IKnowledgeChangeListener {

		private final TriggeredTaskProvider taskProvider;

		public TriggeredTaskListener(TriggeredTaskProvider taskProvider) {
			this.taskProvider = taskProvider;
		}

		@Override
		public void knowledgeChanged(String triggerer, TriggerType recMode) {
			if (recMode.equals(TriggerType.KNOWLEDGE))
				execute(((TriggeredComponentProcessTaskProvider) taskProvider)
						.getTask(triggerer));
			else {
				try {
					Object[] ids = (Object[]) km
							.getKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID);
					TriggeredEnsembleTaskProvider tetp = (TriggeredEnsembleTaskProvider) taskProvider;
					if (recMode.equals(TriggerType.COORDINATOR)) {
						for (Object id : ids)
							execute(tetp.getTask(triggerer, (String) id));
					} else if (recMode.equals(TriggerType.MEMBER)) {
						for (Object id : ids)
							execute(tetp.getTask((String) id, triggerer));
					}
				} catch (KMException kme) {
					Log.e("Knowledge Manager access exception", kme);
				}
			}
		}

		@Override
		public List<String> getKnowledgePaths() {
			List<String> result = new LinkedList<>();
			for (Trigger trigger : taskProvider.getTriggers())
				result.add(((KnowledgeChangeTrigger) trigger)
						.getKnowledgePath().getEvaluatedPath(km,
								PathGrammar.COORD, PathGrammar.MEMBER, null,
								null));
			return result;
		}

	}
}
