package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import kobuki_msgs.Led;

/**
 * Available colors of the turtlebots LEDs.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum LedColor {
	/**
	 * Red color of a LED.
	 */
	RED(Led.RED),
	/**
	 * Green color of a LED.
	 */
	GREEN(Led.GREEN),
	/**
	 * Orange color of a LED (actually combination of green and red).
	 */
	ORANGE(Led.ORANGE),
	/**
	 * Switch the LED off.
	 */
	BLACK(Led.BLACK);

	/**
	 * Byte representation of the LED color for the ROS topic messages.
	 */
	public final byte value;

	/**
	 * Assign a correct byte value to a LED color to represent it in the ROS
	 * topic.
	 * 
	 * @param value The byte value of the color in the ROS topic.
	 */
	private LedColor(byte value) {
		this.value = value;
	}
}
