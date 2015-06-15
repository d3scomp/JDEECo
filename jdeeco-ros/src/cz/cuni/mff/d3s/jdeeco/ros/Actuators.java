package cz.cuni.mff.d3s.jdeeco.ros;

import geometry_msgs.Twist;

import org.ros.concurrent.CancellableLoop;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

/**
 * Provides methods to command actuators through ROS. Registration of
 * appropriate ROS topics is handled in the {@link #subscribe(ConnectedNode)}
 * method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Actuators extends TopicSubscriber {

	/**
	 * The name of the velocity topic.
	 */
	private static final String VELOCITY_TOPIC = "/mobile_base/commands/velocity";
	/**
	 * The maximum absolute linear speed. Value taken from kobuki_keyop
	 */
	private static final double MAX_LINEAR_VELOCITY = 1.5;
	/**
	 * The maximum absolute angular speed. Value taken from kobuki_keyop
	 */
	private static final double MAX_ANGULAR_VELOCITY = 6.6;

	/**
	 * Current linear speed. Range from -{@link #MAX_LINEAR_VELOCITY} to +
	 * {@link #MAX_LINEAR_VELOCITY}.
	 */
	private double linearVelocity;
	/**
	 * Current angular speed. Range from -{@link #MAX_ANGULAR_VELOCITY} to +
	 * {@link #MAX_ANGULAR_VELOCITY}.
	 */
	private double angularVelocity;

	/**
	 * The singleton instance of the {@link Actuators} class.
	 */
	private static Actuators INSTANCE;

	/**
	 * Private constructor enables the {@link Actuators} to be a singleton.
	 */
	private Actuators() {
	}

	/**
	 * Provides the singleton instance of the {@link Actuators}.
	 * 
	 * @return the singleton instance of the {@link Actuators}.
	 */
	public static Actuators getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Actuators();
		}
		return INSTANCE;
	}

	/**
	 * Register and subscribe to required ROS topics for actuator commands.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	void subscribe(ConnectedNode connectedNode) {
		//
		// VELOCITY topic
		//
		final Publisher<Twist> velocityTopic = connectedNode.newPublisher(
				VELOCITY_TOPIC, Twist._TYPE);
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			@Override
			protected void setup() {
				linearVelocity = 0;
				angularVelocity = 0;
			}

			@Override
			protected void loop() throws InterruptedException {
				Twist twist = velocityTopic.newMessage();
				// Set the linear velocity (X axis)
				twist.getLinear().setX(linearVelocity);
				twist.getLinear().setY(0);
				twist.getLinear().setZ(0);
				// Set the angular velocity (Z axis)
				twist.getAngular().setX(0);
				twist.getAngular().setY(0);
				twist.getAngular().setZ(angularVelocity);
				/*
				 * System.out.println(String.format("Velocity: [%f, %f]",
				 * twist.getLinear().getX(), twist.getAngular().getZ())); TODO:
				 * logging
				 */
				velocityTopic.publish(twist);
				Thread.sleep(500); // If the values are not send within 600 ms
									// turtlebot stops
			}
		});
		//
		//
	}

	/**
	 * Set the velocity. Use normalized values from -1 to 1. The sign determines
	 * direction and the value percentage of motor power.
	 * 
	 * @param linear
	 *            The linear velocity. Value in [-1, 1].
	 *            Negative value moves the robot backwards.
	 *            Positive value moves the robot forwards.
	 * @param angular
	 *            The angular velocity. Value in [-1, 1].
	 *            Negative value turns the robot right.
	 *            Positive value turns the robot left.
	 */
	public void setVelocity(double linear, double angular) {
		if(linear < -1) linear = -1;
		if(linear > 1) linear = 1;
		if(angular < -1) angular = -1;
		if(angular > 1) angular = 1;
		
		linearVelocity = fromNormalized(linear, MAX_LINEAR_VELOCITY);
		angularVelocity = fromNormalized(angular, MAX_ANGULAR_VELOCITY);
	}

	/**
	 * Transform the normalized value to absolute using the given maximum.
	 * 
	 * @param normalized
	 *            The value to be transformed. If it's bigger than 1 then 1 is
	 *            used instead. If it's smaller than -1 then -1 is used instead.
	 * @param max
	 *            The maximum absolute value.
	 * @return The absolute value obtained from the normalized by it's
	 *         multiplication with max.
	 */
	private double fromNormalized(double normalized, double max) {
		return normalized * max;
	}

}
