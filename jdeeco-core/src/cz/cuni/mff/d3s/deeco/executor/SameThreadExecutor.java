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
	
	
	@Override
	public synchronized void execute(Task task, Trigger trigger) {
		if (task != null) {
			try {
				task.invoke(trigger);				
			} catch (Exception e) {				
				if (listener != null) {
					Log.w("Task.invoke() failed", e);
					listener.executionFailed(task, e);
				}
				return;
			}		
			
			Log.d("Task.invoke() completed");
			if (listener != null) {
				listener.executionCompleted(task);
			}
		}
	}

	@Override
	public synchronized void setExecutionListener(ExecutionListener listener) {
		this.listener = listener;  
	}

}
