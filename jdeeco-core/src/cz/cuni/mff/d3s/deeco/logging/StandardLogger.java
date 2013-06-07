package cz.cuni.mff.d3s.deeco.logging;

import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Simple wrapper of java.util.logging.Logger with lazy-initialized singleton
 * object and thread-safe methods
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class StandardLogger implements ILogger, Serializable {

	private static ILogger instance;
	private java.util.logging.Logger logger;

	private StandardLogger() {
		CustomLevel.registerCustomLevels();
		logger = java.util.logging.Logger.getLogger(getClass().getPackage().getName());
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream("logging.properties");
		try {
			LogManager.getLogManager().readConfiguration(inputStream);
		} catch (Exception e) {
			logger = java.util.logging.Logger.getLogger("default");
			ConsoleHandler ch = new ConsoleHandler();
			ch.setLevel(CustomLevel.DEBUG);
			logger.addHandler(ch);
			logger.setLevel(CustomLevel.DEBUG);
			logger.severe("Could not load logging.properties file - falling backing to console logging");
		}
	}

	public static ILogger getLogger() {
		if (instance == null) {
			synchronized (StandardLogger.class) {
				if (instance == null) {
					instance = new StandardLogger();
				}
			}
		}
		return instance;
	}

	public synchronized void debug(String s) {
		logger.log(CustomLevel.DEBUG, s);
	}

	public synchronized void debug(String s, Throwable t) {
		logger.log(CustomLevel.DEBUG, s, t);
	}

	public synchronized void info(String s) {
		logger.log(CustomLevel.INFO, s);
	}

	public synchronized void info(String s, Throwable t) {
		logger.log(Level.INFO, s, t);
	}
	
	public synchronized void warning(String s) {
		logger.log(CustomLevel.WARNING, s);
	}

	public synchronized void warning(String s, Throwable t) {
		logger.log(Level.WARNING, s, t);
	}

	public synchronized void error(String s) {
		logger.log(CustomLevel.ERROR, s);
	}

	public synchronized void error(String s, Throwable t) {
		logger.log(CustomLevel.ERROR, s, t);
	}

}
