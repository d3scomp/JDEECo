package cz.cuni.mff.d3s.deeco.scheduling;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.monitoring.Monitor;
import cz.cuni.mff.d3s.deeco.runtime.model.Invocable;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

public abstract class Job extends ParametrizedInstance implements Runnable {

	private final JobExecutionListener listener;
	private Monitor monitor;
	protected boolean cancel;

	public Job(JobExecutionListener listener, KnowledgeManager km) {
		super(km);
		this.listener = listener;
		this.cancel = false;
	}
	
	public void setCancelExecution(boolean cancel) {
		this.cancel = cancel;
	}
	
	public Monitor getMonitor() {
		return monitor;
	}
	
	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
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
