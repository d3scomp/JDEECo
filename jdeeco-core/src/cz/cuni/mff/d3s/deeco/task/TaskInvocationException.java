/**
 * 
 */
package cz.cuni.mff.d3s.deeco.task;

/**
 * Thrown when a task cannot be executed.
 * 
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class TaskInvocationException extends Exception {

	/**
	 * 
	 */
	public TaskInvocationException() {
	}

	/**
	 * @param arg0
	 */
	public TaskInvocationException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public TaskInvocationException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TaskInvocationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public TaskInvocationException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
