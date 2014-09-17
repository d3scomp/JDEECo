package cz.cuni.mff.d3s.deeco.executor;

import java.util.concurrent.Executors;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * Executor that uses a thread pool to execute the given task. 
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class ThreadPooledExecutor implements Executor {

	java.util.concurrent.Executor exec = Executors.newFixedThreadPool(1);
	protected ExecutionListener listener = null;
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * The call is non-blocking; it submits the task for asynchronous execution in a thread pool and returns immediately.
	 * </p>
	 */
	@Override
	public void execute(final Task task, final Trigger trigger) {
		if (task != null) {			
			exec.execute(new Runnable() {
				
				@Override
				public void run() {
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
			});			
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
