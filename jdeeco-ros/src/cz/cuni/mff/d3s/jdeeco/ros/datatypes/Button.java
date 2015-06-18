package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import kobuki_msgs.ButtonEvent;

/**
 * Enumeration of turtlebot's buttons.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum Button {
	/**
	 * Button 0.
	 */
	B0,
	/**
	 * Button 1.
	 */
	B1,
	/**
	 * Button 2.
	 */
	B2;

	/**
	 * Convert the byte representation of the button in ROS messages to the
	 * {@link Button} representation. If the byte doesn't represent any
	 * turtlebot's button a null is returned.
	 * 
	 * @param button
	 *            The byte representation of the button used in ROS messages.
	 * @return the {@link Button} enumeration representation of the button.
	 */
	public static Button fromByte(byte button) {
		switch (button) {
		case ButtonEvent.Button0:
			return Button.B0;
		case ButtonEvent.Button1:
			return Button.B1;
		case ButtonEvent.Button2:
			return Button.B2;
		default:
			return null;
		}
	}
}
