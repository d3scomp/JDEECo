package cz.cuni.mff.d3s.deeco.executor;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * Listener interface for notifications on successful/failed execution via an {@link Executor}.
 * 
 * @see Executor
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public interface ExecutionListener {

	/**
	 * Notifies the listener that the task has been completed.
	 * <p>
	 * After the executed task is finished, this method is called on the listener by the executor to notify that the task
	 * was executed successfully.
	 * </p>
	 * @param task
	 *            the task that has been successfully executed
	 * @param trigger 
	 *            the trigger that triggered the execution of the task
	 */
	void executionCompleted( Task task, Trigger trigger );

	/**
	 * Notifies the listener that the task has been completed with an error.
	 * <p>
	 * This method is identical to {@link #executionCompleted(Task, Trigger)},
	 * however the difference is that this method is used to signal that task execution completed with an error. 
	 * </p>
	 * 
	 * @param task
	 *            the task execution of which failed
	 * @param trigger 
	 *            the trigger that triggered the execution of the task
	 * @param e
	 *            the exception that caused the task execution to fail
	 */
	void executionFailed( Task task, Trigger trigger, Exception e );
}
