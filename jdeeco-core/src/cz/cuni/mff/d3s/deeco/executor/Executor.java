package cz.cuni.mff.d3s.deeco.executor;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * A runtime service that executes the given tasks.
 * 
 * <p>
 * The main point of variability is how the different implementations give the 
 * thread context to the tasks.
 * </p>
 *
 * @see Task#invoke(Trigger)
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public interface Executor {
	/**
	 * Execute the given {@code task} within a specific thread context.
	 * <p>
	 * Depending on the implementation, the call can be both blocking or
	 * non-blocking.
	 * </p>
	 * 
	 * @param task	the task to be executed
	 * @param trigger	the trigger with which the execution was triggered.
	 * 
	 * @see Task#invoke(Trigger)
	 */
	void execute(Task task, Trigger trigger);
	
	/**
	 * Sets the listener to be notified on successful/failed execution.
	 * Only one listener can be registered at any time.
	 * Setting the listener to {@code null} will unregister the previous listener.
	 * 
	 * @param listener a valid reference or {@code null}
	 */
	void setExecutionListener(ExecutionListener listener);
}
