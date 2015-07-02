package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import kobuki_msgs.CliffEvent;

/**
 * Enumeration of the turtlebot's floor sensors.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum FloorSensorID {
	/**
	 * Left side floor sensor.
	 */
	LEFT,
	/**
	 * Right side floor sensor.
	 */
	RIGHT,
	/**
	 * Center floor sensor.
	 */
	CENTER;

	/**
	 * Transform the {@link CliffEvent#getSensor()} state into the
	 * {@link FloorSensorID} instance. The {@link CliffEvent#getSensor()} has
	 * the following states:
	 * <ul>
	 * <li>{@link CliffEvent#LEFT}</li>
	 * <li>{@link CliffEvent#RIGHT}</li>
	 * <li>{@link CliffEvent#CENTER}</li>
	 * </ul>
	 * If a value other the these specified above is passed as an argument null
	 * is returned.
	 * 
	 * @param sensor
	 *            The floor sensor number in a form of byte.
	 * @return The floor sensor ID instance appropriate to the given bumper
	 *         state. Null if the sensor number doesn't correspond to any of the
	 *         values stated in this method description.
	 */
	public static FloorSensorID fromByte(byte sensor) {
		switch (sensor) {
		case CliffEvent.LEFT:
			return FloorSensorID.LEFT;
		case CliffEvent.RIGHT:
			return FloorSensorID.RIGHT;
		case CliffEvent.CENTER:
			return FloorSensorID.CENTER;
		default:
			return null;
		}
	}
}
