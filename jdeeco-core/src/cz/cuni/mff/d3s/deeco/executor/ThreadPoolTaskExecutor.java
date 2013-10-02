package cz.cuni.mff.d3s.deeco.executor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ParameterAccessor;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.LockingMode;
import cz.cuni.mff.d3s.deeco.model.Parameter;
import cz.cuni.mff.d3s.deeco.task.ComponentProcessTask;
import cz.cuni.mff.d3s.deeco.task.EnsembleTask;
import cz.cuni.mff.d3s.deeco.task.Task;

public class ThreadPoolTaskExecutor implements TaskExecutor {

	private final static int THREAD_POOL_SIZE = 10;

	private final ThreadPoolExecutor executor;
	private final KnowledgeManager km;

	public ThreadPoolTaskExecutor(KnowledgeManager km) {
		this(THREAD_POOL_SIZE, km);
	}

	public ThreadPoolTaskExecutor(int corePoolSize, KnowledgeManager km) {
		this.km = km;
		this.executor = new ScheduledThreadPoolExecutor(corePoolSize);
	}

	@Override
	public void submitTask(Task task) {
		if (task instanceof ComponentProcessTask)
			executor.execute(new ComponentProcessTaskExecution(
					(ComponentProcessTask) task, km));
		else if (task instanceof EnsembleTask)
			executor.execute(new EnsembleTaskExecution((EnsembleTask) task, km));
	}

	// ------Private classes-------

	private class ComponentProcessTaskExecution extends ParameterAccessor
			implements Runnable {

		private final ComponentProcessTask task;

		public ComponentProcessTaskExecution(ComponentProcessTask task,
				KnowledgeManager km) {
			super(km);
			this.task = task;
		}

		@Override
		public void run() {
			task.executionStarted();
			LockingMode lm = task.getLockingMode();
			Object[] parameterValues;
			if (lm.equals(LockingMode.STRONG)) {
				ISession session = km.createSession();
				session.begin();
				try {
					while (session.repeat()) {
						parameterValues = getParameterMethodValues(
								task.getParameters(), session);
						try {
							task.execute(parameterValues);
						} catch (Exception e) {
							Log.e("Execution exception " + task + "): ", e);
							session.cancel();
							task.executionException(e);
							break;
						}
						putParameterMethodValues(task.getParameters(),
								parameterValues, session);
						session.end();
					}
				} catch (KMException kme) {
					Log.e("Execution exception", kme);
					session.cancel();
				}
			} else {
				try {
					parameterValues = getParameterMethodValues(task
							.getParameters());
					try {
						task.execute(parameterValues);
					} catch (Exception e) {
						Log.e("Execution exception " + task + "): ", e);
						task.executionException(e);
					}
					putParameterMethodValues(task.getParameters(),
							parameterValues);
				} catch (KMException kme) {
					Log.e("ComponentProcess exception", kme);
				}
			}
			task.executionFinished();
		}

		@Override
		public String getEvaluatedKnowledgePath(Parameter parameter,
				ISession session) {
			return parameter.getKnowledgePath().getEvaluatedPath(km, null,
					null, task.getComponentId(), session);
		}
	}

	private class EnsembleTaskExecution extends ParameterAccessor implements
			Runnable {

		private final EnsembleTask task;

		public EnsembleTaskExecution(EnsembleTask task, KnowledgeManager km) {
			super(km);
			this.task = task;
		}

		@Override
		public void run() {
			task.executionStarted();
			Object[] parameterValues;
			ISession session = km.createSession();
			session.begin();
			while (session.repeat()) {
				try {
					if (evaluateMembership(session)) {
						parameterValues = getParameterMethodValues(
								task.getKnowledgeExchangeParameters(), session);
						try {
							task.executeKnowledgeExchange(parameterValues);
						} catch (Exception e) {
							Log.e("Execution exception " + task + "): ", e);
							session.cancel();
							task.executionException(e);
							break;
						}
						putParameterMethodValues(
								task.getKnowledgeExchangeParameters(),
								parameterValues, session);
					}
				} catch (KMException kme) {
					Log.e("Ensemble exception", kme);
					session.cancel();
					return;
				}
				session.end();
			}
			task.executionFinished();
		}

		@Override
		public String getEvaluatedKnowledgePath(Parameter parameter,
				ISession session) {
			return parameter.getKnowledgePath().getEvaluatedPath(km,
					task.getCoordinator(), task.getMember(), null, session);
		}

		private boolean evaluateMembership(ISession session) {
			try {
				Object[] parameterValues = getParameterMethodValues(
						task.getMembershipParameters(), session);
				try {
					return task.executeMembership(parameterValues);
				} catch (Exception e) {
					Log.e("Ensemble exception while membership evaluation", e);
					task.executionException(e);
					return false;
				}
			} catch (KMException kme) {
				return false;
			}
		}

	}

}
