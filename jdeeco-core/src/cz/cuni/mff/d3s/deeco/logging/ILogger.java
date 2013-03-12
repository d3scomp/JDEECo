package cz.cuni.mff.d3s.deeco.logging;

/**
 * Common abstraction for OSGi-based & non OSGi-based Loggers exposing 3 logging
 * methods for 3 logging levels.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public interface ILogger {

	/**
	 * Mainly for debugging reasons.
	 * 
	 * @param msg
	 *            the string message
	 */
	public void fine(String msg);

	/**
	 * Log on the FINE level with associated Throwable information (exception
	 * message and stack trace).
	 * 
	 * @param msg
	 * @param thrown
	 */
	public void fine(String msg, Throwable thrown);

	/**
	 * General logging method.
	 * 
	 * @param msg
	 *            the string message
	 */
	public void info(String msg);

	/**
	 * Log on the INFO level with associated Throwable information (exception
	 * message and stack trace).
	 * 
	 * @param msg
	 * @param thrown
	 */
	public void info(String msg, Throwable thrown);

	/**
	 * For reporting exceptions & errors without the stack trace.
	 * 
	 * @param msg
	 *            the string message
	 */
	public void severe(String msg);

	/**
	 * Log on the SEVERE level with associated Throwable information (exception
	 * message and stack trace).
	 * 
	 * @param msg
	 *            the (custom) string message, if any
	 * @param thrown
	 *            Throwable object (e.g. an Exception)
	 */
	public void severe(String msg, Throwable thrown);
}
