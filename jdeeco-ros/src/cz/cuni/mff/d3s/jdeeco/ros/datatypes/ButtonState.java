package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import kobuki_msgs.ButtonEvent;

/**
 * Enumeration of turtlebot's possible button states.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum ButtonState {
	/**
	 * The button is pressed.
	 */
	PRESSED,
	/**
	 * The button is released.
	 */
	RELEASED;

	/**
	 * Convert the given byte representation of the button state used in ROS
	 * messages into {@link ButtonState} representation.
	 * 
	 * @param state
	 *            The byte representation of the button state used in ROS
	 *            messages.
	 * @return the enumeration representation of the button state.
	 */
	public static ButtonState fromByte(byte state) {
		switch (state) {
		case ButtonEvent.PRESSED:
			return ButtonState.PRESSED;
		case ButtonEvent.RELEASED:
			return ButtonState.RELEASED;
		default:
			return null;
		}
	}
}
