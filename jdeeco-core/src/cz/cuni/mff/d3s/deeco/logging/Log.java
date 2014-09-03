package cz.cuni.mff.d3s.deeco.logging;

/**
 * API for sending log output.
 * 
 * There are 4 logging levels: DEBUG(d) < INFO(i) < WARNING(w) < ERROR(e)
 * 
 * Could be extended in the future to choose between Standard- and OSGiLogger at
 * runtime in the <code>getLogger</code> method.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class Log {

	private static Logger getLogger() {
		return StandardLogger.getLogger();
	}

	public static void d(String msg) {
		getLogger().debug(msg);
	}

	public static void d(String msg, Throwable t) {
		getLogger().debug(msg, t);
	}
	
	public static boolean isDebugLoggable() {
		return getLogger().isDebugLoggable();
	}

	public static void i(String msg) {
		getLogger().info(msg);
	}

	public static void i(String msg, Throwable t) {
		getLogger().info(msg, t);
	}

	public static void w(String msg) {
		getLogger().warning(msg);
	}

	public static void w(String msg, Throwable t) {
		getLogger().warning(msg, t);
	}

	public static void e(String msg) {
		getLogger().error(msg);
	}

	public static void e(String msg, Throwable t) {
		getLogger().error(msg, t);
	}
}
