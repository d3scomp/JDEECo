package cz.cuni.mff.d3s.deeco.logging;

/**
 * Chooses between Standard- and OSGiLogger (once we have the last one), based
 * on runtime information. For now, it just returns the StandardLogger object.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class LoggerFactory {

	public static ILogger getLogger() {
		return StandardLogger.getLogger();
	}
}
