/**
 * 
 */
package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;

/**
 * Thrown when a task (i.e. process method, membership condition, or knowledge exchange) cannot be executed.
 * 
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class TaskInvocationException extends DEECoException {
	private static final long serialVersionUID = 1L;

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
}
