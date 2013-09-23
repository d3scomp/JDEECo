package cz.cuni.mff.d3s.deeco.scheduling;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.monitoring.Monitor;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.runtime.model.Invocable;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

public abstract class Job extends ParametrizedInstance implements Runnable {

	private static ThreadLocal<Runtime> runtime = new ThreadLocal<>();

	private final Monitor monitor;
	private final JobExecutionListener listener;
	private final Runtime actualRuntime;

	public Job(Runtime runtime, Monitor monitor, JobExecutionListener listener) {
		super(runtime.getKnowledgeManager());
		this.monitor = monitor;
		this.listener = listener;
		this.actualRuntime = runtime;
	}
	
	public Monitor getMonitor() {
		return monitor;
	}

	public static Runtime getRuntime() {
		return runtime.get();
	}

	public abstract Schedule getSchedule();
	public abstract String getInstanceId();
	public abstract String getModelId();

	protected void evaluateMethod(Invocable invocable, ISession session)
			throws Exception {
		Job.runtime.set(actualRuntime);
		Object[] processParameters = getParameterMethodValues(invocable,
				session);
		Method m = invocable.getMethod();
		m.invoke(null, processParameters);
		putParameterMethodValues(invocable, processParameters, session);
	}

	protected void jobExecutionStarted() {
		if (monitor != null && monitor instanceof JobExecutionListener)
			((JobExecutionListener) monitor).jobExecutionStarted(this);
		if (listener != null)
				listener.jobExecutionStarted(this);
	}

	protected void jobExecutionFinished() {
		if (monitor != null && monitor instanceof JobExecutionListener)
			((JobExecutionListener) monitor).jobExecutionFinished(this);
		if (listener != null)
				listener.jobExecutionFinished(this);
	}
	
	protected void jobExecutionException(Throwable t) {
		if (monitor != null && monitor instanceof JobExecutionListener)
			((JobExecutionListener) monitor).jobExecutionException(this, t);
		if (listener != null)
				listener.jobExecutionException(this, t);
	}
}
