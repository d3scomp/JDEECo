package cz.cuni.mff.d3s.deeco.simulation.scheduler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.scheduler.SchedulerEvent;
import cz.cuni.mff.d3s.deeco.simulation.CallbackProvider;
import cz.cuni.mff.d3s.deeco.simulation.SimulationTimeEventListener;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.timer.Timer;

/**
 * TODO Remove this class, it is no more needed since we have a new scheduler in core.
 * 
 * The {@link Scheduler} implementation designed for single threaded simulation.
 * This scheduler is suppose to be driven by the simulation through
 * {@link SimulationTimeEventListener} methods.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class SimulationScheduler implements Scheduler,
		SimulationTimeEventListener {

	private final AbstractHost host;
	private final CallbackProvider callbackProvider;

	private final TreeSet<SchedulerEvent> queue;
	private final Set<Task> allTasks;
	private final Map<Task, SchedulerEvent> periodicEvents;

	private final Set<Trigger> onTriggerSchedules;

	private Executor executor;

	public SimulationScheduler(AbstractHost host,
			CallbackProvider callbackProvider) {
		this.host = host;
		this.callbackProvider = callbackProvider;
		queue = new TreeSet<SchedulerEvent>();
		allTasks = new HashSet<>();
		periodicEvents = new HashMap<>();
		onTriggerSchedules = new HashSet<>();
	}
	
	public AbstractHost getHost() {
		return host;
	}

	@Override
	public void addTask(Task task) {
		if (task == null)
			throw new IllegalArgumentException("The task cannot be null");
		if (allTasks.contains(task))
			return;
		if (task.getTimeTrigger() != null) {
			SchedulerEvent event = new SchedulerEvent(task,
					task.getTimeTrigger());

			// for experiments, publisher task has a random start offset up to
			// its period
			if (event.periodic) {
				Log.d(String.format(
						"Scheduler init: Periodic task %s offset %d", task
								.getClass().toString(), task.getTimeTrigger()
								.getOffset()));
			}

			scheduleAfter(event, task.getTimeTrigger().getOffset());

			periodicEvents.put(task, event);
		}
		task.setTriggerListener(new TaskTriggerListener() {
			@Override
			public void triggered(Task task, Trigger trigger) {
				if (allTasks.contains(task)) {
					// if the trigger has been already scheduled (i.e., there
					// have
					// been many consecutive invocations of that trigger in a
					// row),
					// then skip this event
					if (onTriggerSchedules.contains(trigger))
						return;
					onTriggerSchedules.add(trigger);
					// schedule immediately, regardless the actual runtime of
					// the process that triggered this trigger
					scheduleAfter(new SchedulerEvent(task, trigger), 0);
				}
			}
		});

		allTasks.add(task);

	}

	@Override
	public void removeTask(Task task) {
		if (!allTasks.contains(task))
			return;
		task.unsetTriggerListener();
		List<SchedulerEvent> remove = new LinkedList<>();
		for (SchedulerEvent se : queue) {
			if (se.executable.equals(task)) {
				remove.add(se);
			}
		}
		queue.removeAll(remove);
		periodicEvents.remove(task);
		allTasks.remove(task);
	}

	@Override
	public void executionFailed(Task task, Trigger trigger, Exception e) {
		Log.e(e.getMessage());
		executionCompleted(task, trigger);
	}

	@Override
	public void executionCompleted(Task task, Trigger trigger) {
		onTriggerSchedules.remove(trigger);
		registerNextExecution(false);
	}

	@Override
	public void setExecutor(Executor executor) {
		this.executor = executor;
		if (this.executor != null)
			this.executor.setExecutionListener(this);
	}


	@Override
	public void at(long time) {
		if (Log.isDebugLoggable()) {
			Log.d("Scheduler " + host.getHostId()+" at: "+time+" called with queue: " + Arrays.toString(queue.toArray()));
		}
		
		SchedulerEvent event;
		while ((event = queue.first()) != null) {
			// if notified too early (or already processed all events scheduled
			// for the current time)
			if (event.nextExecutionTime > time)
				break;

			// The time is right to execute the next task
			pop();
			if (event.periodic) {
				// schedule for the next period
				// add a random offset within the period (up to 75% of the
				// period)
				TimeTrigger timeTrigger = event.executable.getTimeTrigger();
				event.nextPeriodStart += timeTrigger.getPeriod();
				event.nextExecutionTime = event.nextPeriodStart;
				push(event);
			}
			if (executor != null) {
				executor.execute(event.executable, event.trigger);
			} else {
				Log.e("The simulation scheduler is associated with no excecutor!");
			}
		}
	}

	// ------Private methods--------

	// private void scheduleNow(SchedulerEvent event, long period) {
	// event.period = period;
	// event.nextExecutionTime = host.getCurrentTime() +
	// lastProcessExecutionTime;
	// push(event);
	// }

	/**
	 * Note that this method has to be explicitly protected by queue's monitor!
	 */
	void scheduleAfter(SchedulerEvent event, long delay) {
		event.nextExecutionTime = host.getCurrentMilliseconds() + delay;
		event.nextPeriodStart = host.getCurrentMilliseconds() + delay;
		push(event);
	}

	private void registerNextExecution(boolean firstExecution) {
		if (!queue.isEmpty()) {
			long nextExecutionTime = queue.first().nextExecutionTime;
			if (!firstExecution && nextExecutionTime <= host.getCurrentMilliseconds()) {
				return; // nextExecutionTime = host.getSimulationTime() + 1;
			}
			callbackProvider.callAt(nextExecutionTime, host.getHostId());
			// System.out.println("Scheduler " + host.getId() +
			// " registering callback at " + nextExecutionTime);
		}
	}

	private SchedulerEvent pop() {
		return queue.pollFirst();
	}

	private void push(SchedulerEvent event) {
		// Log.d("Adding: " + event);

		queue.add(event);
		

		// Log.d("Queue: " + queue);

		// TODO take into account different scheduling policies and WCET of
		// tasks. According to those the tasks need to be rescheduled.
	}
	
	public String toString() {
		return "SimulationScheduler of " + host.getHostId();
	}

	@Override
	public Timer getTimer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHostId() {
		// TODO: Temporary solution
		return Integer.parseInt(getHost().getHostId());
	}

}
