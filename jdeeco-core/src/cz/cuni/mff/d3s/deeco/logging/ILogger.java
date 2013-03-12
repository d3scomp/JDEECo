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
	 * For reporting exceptions & errors.
	 * 
	 * @param msg
	 *            the string message
	 */
	public void severe(String msg);

	/**
	 * General logging method.
	 * 
	 * @param msg
	 *            the string message
	 */
	public void info(String msg);

	/**
	 * For debugging reasons.
	 * 
	 * @param msg
	 *            the string message
	 */
	public void fine(String msg);

}
