package cz.cuni.mff.d3s.deeco.logging;

/**
 * Common abstraction for OSGi-based & non OSGi-based Loggers.
 * 
 * There are 4 logging levels: DEBUG(d) < INFO(i) < WARNING(w) < ERROR(e)
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
interface Logger {
	
	/**
	 * Mainly for debugging reasons.
	 * 
	 * @param msg
	 *            the string message
	 */
	void debug(String msg);
	
	boolean isDebugLoggable();

	/**
	 * Log on the DEBUG level with associated Throwable information (exception
	 * message and stack trace).
	 * 
	 * @param msg
	 *            the string message
	 * @param thrown
	 *            Throwable object (e.g. an Exception)
	 */
	void debug(String msg, Throwable thrown);

	/**
	 * General logging method.
	 * 
	 * @param msg
	 *            the string message
	 */
	void info(String msg);

	/**
	 * Log on the INFO level with associated Throwable information (exception
	 * message and stack trace).
	 * 
	 * @param msg
	 *            the string message
	 * @param thrown
	 *            Throwable object (e.g. an Exception)
	 */
	void info(String msg, Throwable thrown);

	/**
	 * For warnings (soft errors)
	 * 
	 * @param msg
	 *            the string message
	 */
	void warning(String msg);

	/**
	 * Log on the WARNING level with associated Throwable information (exception
	 * message and stack trace).
	 * 
	 * @param msg
	 *            the string message
	 * @param thrown
	 *            Throwable object (e.g. an Exception)
	 */
	void warning(String msg, Throwable thrown);

	/**
	 * For reporting exceptions & errors without the stack trace.
	 * 
	 * @param msg
	 *            the string message
	 */
	void error(String msg);

	/**
	 * Log on the ERROR level with associated Throwable information (exception
	 * message and stack trace).
	 * 
	 * @param msg
	 *            the string message
	 * @param thrown
	 *            Throwable object (e.g. an Exception)
	 */
	void error(String msg, Throwable thrown);
}
