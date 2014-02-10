package cz.cuni.mff.d3s.deeco.executor;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * Executor that reuses the thread context in which the {@link #execute(Task)} was called. 
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class SameThreadExecutor implements Executor {

	protected ExecutionListener listener = null;
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * The call is blocking, executes the task in the thread, which the method was called in.
	 * </p>
	 */
	@Override
	public void execute(Task task, Trigger trigger) {
		if (task != null) {
			try {
				task.invoke(trigger);				
			} catch (Exception e) {				
				if (listener != null) {
					Log.w("Task.invoke() failed", e);
					listener.executionFailed(task, trigger, e);
				}
				return;
			}			
			
			if (listener != null) {
				listener.executionCompleted(task, trigger);
			}
		}
	}

	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.executor.Executor#setExecutionListener(cz.cuni.mff.d3s.deeco.executor.ExecutionListener)
	 */
	@Override
	public void setExecutionListener(ExecutionListener listener) {
		this.listener = listener;  
	}

}
