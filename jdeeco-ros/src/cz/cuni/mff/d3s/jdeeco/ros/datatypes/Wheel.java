package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import kobuki_msgs.WheelDropEvent;

/**
 * Enumeration of turtlebot's wheels.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum Wheel {
	/**
	 * The left wheel.
	 */
	LEFT,
	/**
	 * The right wheel.
	 */
	RIGHT;

	/**
	 * Transform the wheel encoded in a byte from the
	 * {@link WheelDropEvent#getWheel()} method into an instance of
	 * {@link Wheel}. If the wheel parameter doesn't equal to either
	 * {@link WheelDropEvent.LEFT} or {@link WheelDropEvent.RIGHT} then null is
	 * returned.
	 * 
	 * @param wheel
	 *            The byte representation of the wheel.
	 * @return An instance of the enumeration of turtlebot's wheels or null.
	 */
	public static Wheel fromByte(byte wheel) {
		switch (wheel) {
		case WheelDropEvent.LEFT:
			return Wheel.LEFT;
		case WheelDropEvent.RIGHT:
			return Wheel.RIGHT;
		default:
			return null;
		}
	}
}
