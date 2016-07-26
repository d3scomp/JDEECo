package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import kobuki_msgs.BumperEvent;

/**
 * Enumeration of the states of turtlebot's bumper.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum BumperValue {
	/**
	 * Left side of the bumper is pressed.
	 */
	LEFT,
	/**
	 * Right side of the bumper is pressed.
	 */
	RIGHT,
	/**
	 * Center of the bumper if pressed.
	 */
	CENTER,
	/**
	 * The bumper is released.
	 */
	RELEASED;

	/**
	 * Transform the {@link BumperEvent#getBumper()} state into the
	 * {@link BumperValue} instance. The {@link BumperEvent#getBumper()} has the
	 * following states:
	 * <ul>
	 * <li>{@link BumperEvent#LEFT}</li>
	 * <li>{@link BumperEvent#RIGHT}</li>
	 * <li>{@link BumperEvent#CENTER}</li>
	 * </ul>
	 * If a value other the these specified above is passed as an argument
	 * {@link BumperValue#CENTER} is returned.
	 * 
	 * @param state
	 *            The bumper state in a form of byte.
	 * @return The bumper enum instance appropriate to the given bumper state.
	 */
	public static BumperValue fromByte(byte state) {
		switch (state) {
		case BumperEvent.LEFT:
			return BumperValue.LEFT;
		case BumperEvent.RIGHT:
			return BumperValue.RIGHT;
		default:
			return BumperValue.CENTER;
		}
	}
}
