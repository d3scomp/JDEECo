package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

/**
 * The enumeration of sounds reproducible by turtlebot.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum SoundValue {
	/**
	 * Sound of a button being pressed.
	 */
	BUTTON(kobuki_msgs.Sound.BUTTON),
	/**
	 * Sound of the end of a cleaning.
	 */
	CLEANING_END(kobuki_msgs.Sound.CLEANINGEND),
	/**
	 * Sound of the start of a cleaning.
	 */
	CLEANING_START(kobuki_msgs.Sound.CLEANINGSTART),
	/**
	 * An error sound.
	 */
	ERROR(kobuki_msgs.Sound.ERROR),
	/**
	 * Sound of switching the robot off.
	 */
	OFF(kobuki_msgs.Sound.OFF),
	/**
	 * Sound of switching the robot on.
	 */
	ON(kobuki_msgs.Sound.ON),
	/**
	 * Sound of recharging the robot.
	 */
	RECHARGE(kobuki_msgs.Sound.RECHARGE);

	/**
	 * The byte representation of the sound in the ROS topic.
	 */
	public final byte value;

	/**
	 * Assign appropriate value to the sound.
	 * 
	 * @param value
	 *            Byte representation of the sound in the ROS topic.
	 */
	private SoundValue(byte value) {
		this.value = value;
	}
}
