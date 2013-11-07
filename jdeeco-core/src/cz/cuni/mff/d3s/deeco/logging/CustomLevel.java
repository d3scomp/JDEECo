package cz.cuni.mff.d3s.deeco.logging;

import java.util.logging.Level;

/**
 * Class defining two additional levels (DEBUG,ERROR) to the logging levels of
 * {@link java.util.logging.Level} class.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
final class CustomLevel extends Level {

	private static final long serialVersionUID = -3922922184407799203L;

	public static final Level DEBUG = new CustomLevel("DEBUG",
			Level.FINE.intValue() + 1);

	public static final Level ERROR = new CustomLevel("ERROR",
			Level.SEVERE.intValue() + 1);

	protected CustomLevel(String name, int value) {
		super(name, value);
	}

	public static void registerCustomLevels() {
	}

}
