package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import java.io.Serializable;

/**
 * Encapsulates values that determine the velocity of Turtlebot. The velocity is
 * divided into two values: linear velocity and angular velocity. The final
 * velocity of Turtlebot is the combination of these two. Both these velocities
 * are expressed in normalized form ranging from -1 to 1 where 0 indicates no
 * movement. The sign determines direction and the value percentage of motor power.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class Velocity implements Serializable {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 3637865039454051476L;

	/**
	 * Expresses no movement. Applicable both to the {@link #linear} and
	 * {@link #angular} velocity.
	 */
	public static final double STOPPED = 0;

	/**
	 * Expresses the maximal speed forward. Applicable to the {@link #linear}
	 * velocity.
	 */
	public static final double MAX_FORWARD = 1;

	/**
	 * Expresses the maximal speed backward. Applicable to the {@link #linear}
	 * velocity.
	 */
	public static final double MAX_BACKWARD = -1;

	/**
	 * Expresses the maximal speed of rotation to the right. Applicable to the
	 * {@link #angular} velocity.
	 */
	public static final double MAX_RIGHT = 1;

	/**
	 * Expresses the maximal speed of rotation to the left. Applicable to the
	 * {@link #angular} velocity.
	 */
	public static final double MAX_LEFT = -1;

	/**
	 * The linear velocity of Turtlebot. Ranging from {@value #MAX_BACKWARD}
	 * ({@link #MAX_BACKWARD}) to {@value #MAX_FORWARD} ({@link #MAX_FORWARD})
	 * where {@value #STOPPED} ({@link #STOPPED}) expresses no movement. The
	 * final movement of Turtlebot is based on the combination of
	 * {@link #linear} and {@link #angular} velocity.
	 */
	public final double linear;

	/**
	 * The angular velocity of the Turtlebot. Ranging from {@value #MAX_LEFT}
	 * ({@link #MAX_LEFT}) to {@value #MAX_RIGHT} ({@link #MAX_RIGHT}) where
	 * {@value #STOPPED} ({@link #STOPPED}) expresses no rotation. The final
	 * movement of Turtlebot is based on the combination of {@link #linear} and
	 * {@link #angular} velocity.
	 */
	public final double angular;

	/**
	 * Create new velocity for Turtlebot. If either of the provided velocities
	 * is out of range the nearest velocity boundary is assigned instead of it.
	 * 
	 * @param linear
	 *            The linear velocity. The allowed range is from
	 *            {@value #MAX_BACKWARD} ({@link #MAX_BACKWARD}) to
	 *            {@value #MAX_FORWARD} ({@link #MAX_FORWARD}) where
	 *            {@value #STOPPED} ({@link #STOPPED}) indicates no movement.
	 * @param angular
	 *            The angular velocity. allowed range is from {@value #MAX_LEFT}
	 *            ({@link #MAX_LEFT}) to {@value #MAX_RIGHT} ({@link #MAX_RIGHT})
	 *            where {@value #STOPPED} ({@link #STOPPED}) indicates no
	 *            movement.
	 */
	public Velocity(double linear, double angular) {
		if (linear < MAX_BACKWARD) {
			linear = MAX_BACKWARD;
		}
		if (linear > MAX_FORWARD) {
			linear = MAX_FORWARD;
		}
		if (angular < MAX_LEFT) {
			angular = MAX_LEFT;
		}
		if (angular > MAX_RIGHT) {
			angular = MAX_RIGHT;
		}
		
		this.linear = linear;
		this.angular = angular;
	}
}
