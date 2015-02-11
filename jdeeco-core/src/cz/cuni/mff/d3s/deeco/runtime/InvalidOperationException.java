package cz.cuni.mff.d3s.deeco.runtime;

/**
 * Thrown when the user invoked a method that is not valid for the current state of the application (e.g. stop on a stopped DEECo). 
 * @author Filip Krijt <krijt@d3s.mff.cuni.cz>
 *
 */
public class InvalidOperationException extends DEECoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidOperationException(String method) {
		super("Attempted to call a method (" + method + ") invalid for the current state of the DEECo application.");
	}
}
