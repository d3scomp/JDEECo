package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

/**
 * Enumeration of turtlebot's motor power states.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum MotorPower {
	/**
	 * The power to motors is switched on.
	 */
	ON(kobuki_msgs.MotorPower.ON),
	/**
	 * The power to motors is switched off.
	 */
	OFF(kobuki_msgs.MotorPower.OFF);

	/**
	 * The byte representation of the enumerated value in ROS messages.
	 */
	public final byte value;

	/**
	 * Associate appropriate byte value with the enumerated motor power value.
	 * 
	 * @param value
	 *            the byte representation of the motor power value in ROS
	 *            messages.
	 */
	private MotorPower(byte value) {
		this.value = value;
	}

	/**
	 * Convert the byte representation of the motor power value from ROS
	 * messages into the {@link MotorPower} enumeration value.
	 * 
	 * @param motorPower the byte representation of the motor power value.
	 * @return {@link MotorPower} enumeration representation of the motor power value.
	 */
	public static MotorPower fromByte(byte motorPower) {
		switch (motorPower) {
		case kobuki_msgs.MotorPower.ON:
			return MotorPower.ON;
		case kobuki_msgs.MotorPower.OFF:
			return MotorPower.OFF;
		default:
			return null;
		}
	}

}
