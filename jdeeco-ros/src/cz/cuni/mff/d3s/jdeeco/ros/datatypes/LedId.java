package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

/**
 * Enumeration of available LEDs on the turtlebot.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum LedId {
	/**
	 * The LED1.
	 */
	LED1("/mobile_base/commands/led1"),
	/**
	 * The LED2.
	 */
	LED2("/mobile_base/commands/led2");
	
	/**
	 * The name of the ROS topic belonging to the LED.
	 */
	public final String topic;
	
	/**
	 * Assign the correct ROS topic to the LED.
	 * @param topic the ROS topic for the LED.
	 */
	private LedId(String topic){
		this.topic = topic;
	}
}
