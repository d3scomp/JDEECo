package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import kobuki_msgs.CliffEvent;

/**
 * Enumeration of the states of turtlebot's floor sensors.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum FloorSensorState {
	/**
	 * The floor sensor senses the floor.
	 */
	FLOOR,
	/**
	 * The floor sensor senses a cliff.
	 */
	CLIFF;

	/**
	 * Transform the {@link CliffEvent#getState()} state into the
	 * {@link FloorSensorState} instance. The {@link CliffEvent#getState()} has the
	 * following states:
	 * <ul>
	 * <li>{@link CliffEvent#FLOOR}</li>
	 * <li>{@link CliffEvent#CLIFF}</li>
	 * </ul>
	 * If a value other the these specified above is passed as an argument
	 * null is returned.
	 * 
	 * @param state
	 *            The floor sensor state in a form of byte.
	 * @return The floor sensor state enum instance appropriate to the given floor sensor state.
	 */
	public static FloorSensorState fromByte(byte state) {
		switch (state) {
		case CliffEvent.FLOOR:
			return FloorSensorState.FLOOR;
		case CliffEvent.CLIFF:
			return FloorSensorState.CLIFF;
		default:
			return null;
		}
	}
}
