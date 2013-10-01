package cz.cuni.mff.d3s.deeco.scheduling;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.executor.ExecutionListener;
import cz.cuni.mff.d3s.deeco.runtime.model.Invocable;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

public abstract class Task {

	private ExecutionListener listener;

	public abstract Schedule getSchedule();

	protected Object execute(Invocable invocable, Object[] parameters)
			throws Exception {
		Method m = invocable.getMethod();
		return m.invoke(null, parameters);
	}
	
	public void setListener(ExecutionListener listener) {
		this.listener = listener;
	}

	public void executionStarted() {
		if (listener != null)
			listener.executionStarted(this);
	}

	public void executionFinished() {
		if (listener != null)
			listener.executionFinished(this);
	}

	public void executionException(Throwable t) {
		if (listener != null)
			listener.executionException(this, t);
	}
}
