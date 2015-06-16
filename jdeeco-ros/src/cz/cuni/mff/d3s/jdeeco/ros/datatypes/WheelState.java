package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import kobuki_msgs.WheelDropEvent;

/**
 * The enumeration of turtlebot's possible wheel states.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum WheelState {
	/**
	 * The wheel is dropped.
	 */
	DROPPED,
	/**
	 * The wheel is raised.
	 */
	RAISED;

	/**
	 * Transform the given byte representation of the wheel state from the
	 * {@link WheelDropEvent#getState()} method into an instance of
	 * {@link WheelState}. If the given argument doesn't equal to either
	 * {@link WheelDropEvent.DROPPED} or {@link WheelDropEvent.RAISED} then null
	 * is returned.
	 * 
	 * @param state The byte representation of the turtlebot's wheel state.
	 * @return The state of the wheel or null.
	 */
	public static WheelState fromByte(byte state) {
		switch (state) {
		case WheelDropEvent.DROPPED:
			return WheelState.DROPPED;
		case WheelDropEvent.RAISED:
			return WheelState.RAISED;
		default:
			return null;
		}
	}
}
