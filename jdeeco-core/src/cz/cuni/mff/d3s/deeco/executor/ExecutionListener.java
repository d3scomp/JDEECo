package cz.cuni.mff.d3s.deeco.executor;

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
	 * Called if the {@code task} was successfully executed.
	 * 
	 * @param task
	 *            the task that has been successfully executed
	 */
	void executionCompleted( Task task );

	/**
	 * Called if execution of the {@code task} failed.
	 * 
	 * @param task
	 *            the task execution of which failed
	 * @param e
	 *            the exception that caused the task execution to fail
	 */
	void executionFailed( Task task, Exception e );
}
