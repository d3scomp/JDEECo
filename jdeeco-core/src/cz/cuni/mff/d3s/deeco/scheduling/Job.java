package cz.cuni.mff.d3s.deeco.scheduling;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.runtime.model.Invocable;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

public abstract class Job extends ParametrizedInstance implements Runnable {

	private static ThreadLocal<Runtime> runtime;

	private JobExecutionListener listener;

	public Job(Runtime runtime, JobExecutionListener listener) {
		super(runtime.getKnowledgeManager());
		this.listener = listener;
		Job.runtime.set(runtime);
	}

	public static Runtime getRuntime() {
		return runtime.get();
	}

	public abstract Schedule getSchedule();
	public abstract String getInstanceId();
	public abstract String getModelId();

	protected void evaluateMethod(Invocable invocable, ISession session)
			throws Exception {
		Object[] processParameters = getParameterMethodValues(invocable,
				session);
		Method m = invocable.getMethod();
		m.invoke(null, processParameters);
		putParameterMethodValues(invocable, processParameters, session);
	}

	protected void jobExecutionStarted() {
		if (listener != null)
				listener.jobExecutionStarted(this);
	}

	protected void jobExecutionFinished() {
		if (listener != null)
				listener.jobExecutionFinished(this);
	}
	
	protected void jobExecutionException(Throwable t) {
		if (listener != null)
				listener.jobExecutionException(this, t);
	}
}
