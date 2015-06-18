package cz.cuni.mff.d3s.jdeeco.ros;

import geometry_msgs.Twist;

import java.util.HashMap;
import java.util.Map;

import kobuki_msgs.Led;

import org.ros.concurrent.CancellableLoop;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedColor;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedId;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Sound;

/**
 * Provides methods to command actuators through ROS. Registration of
 * appropriate ROS topics is handled in the {@link #subscribe(ConnectedNode)}
 * method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Actuators extends TopicSubscriber {
	// TODO: document rosjava types that are seen by the user of this interface

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
	 * A {@link Map} of available LEDs and theirs assigned colors.
	 */
	private Map<LedId, LedColor> ledColor;

	/**
	 * The name of the topic for sound messages.
	 */
	private static final String SOUND_TOPIC = "/mobile_base/commands/sound";
	/**
	 * The sound to be played.
	 */
	private Sound sound;
	/**
	 * The lock to wait and notify on when a sound should be played.
	 */
	private Object soundLock;
	

	/**
	 * Internal constructor enables the {@link Actuators} to be a singleton.
	 */
	Actuators() {
		ledColor = new HashMap<>();
		soundLock = new Object();
	}

	/**
	 * Register and subscribe to required ROS topics for actuator commands.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	void subscribe(ConnectedNode connectedNode) {
		subscribeVelocity(connectedNode);
		subscribeLed(connectedNode);
		subscribeSound(connectedNode);
	}

	/**
	 * Subscribe to the ROS topic for velocity.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeVelocity(ConnectedNode connectedNode) {
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
	}

	/**
	 * Subscribe all the LEDs to ROS topics for them. To publish color changes
	 * wait until notified by the {@link #setLed(LedId, LedColor)} setter.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeLed(ConnectedNode connectedNode) {
		for (LedId ledId : LedId.values()) {
			final Publisher<Led> ledTopic = connectedNode.newPublisher(
					ledId.topic, Led._TYPE);
			connectedNode.executeCancellableLoop(new CancellableLoop() {
				@Override
				protected void setup() {
					ledColor.put(ledId, LedColor.BLACK);
				}

				@Override
				protected void loop() throws InterruptedException {
					Led led = ledTopic.newMessage();
					led.setValue(ledColor.get(ledId).value);
					ledTopic.publish(led);
					// TODO: log

					synchronized (ledId) {
						ledId.wait();
					}
				}
			});
		}
	}

	/**
	 * Subscribe to the ROS topics for sound messages. To publish sound message
	 * wait until notified by the {@link #playSound(Sound)} setter.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeSound(ConnectedNode connectedNode) {
		final Publisher<kobuki_msgs.Sound> soundTopic = connectedNode
				.newPublisher(SOUND_TOPIC, kobuki_msgs.Sound._TYPE);
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			@Override
			protected void setup() {
			}

			@Override
			protected void loop() throws InterruptedException {

				synchronized (soundLock) {
					soundLock.wait();
				}

				kobuki_msgs.Sound soundMsg = soundTopic.newMessage();
				soundMsg.setValue(sound.value);
				soundTopic.publish(soundMsg);
				// System.out.println("sound: " + sound);
				// TODO: log

			}
		});
	}

	/**
	 * Set the velocity. Use normalized values from -1 to 1. The sign determines
	 * direction and the value percentage of motor power.
	 * 
	 * @param linear
	 *            The linear velocity. Value in [-1, 1]. Negative value moves
	 *            the robot backwards. Positive value moves the robot forwards.
	 * @param angular
	 *            The angular velocity. Value in [-1, 1]. Negative value turns
	 *            the robot right. Positive value turns the robot left.
	 */
	public void setVelocity(double linear, double angular) {
		if (linear < -1)
			linear = -1;
		if (linear > 1)
			linear = 1;
		if (angular < -1)
			angular = -1;
		if (angular > 1)
			angular = 1;

		linearVelocity = fromNormalized(linear, MAX_LINEAR_VELOCITY);
		angularVelocity = fromNormalized(angular, MAX_ANGULAR_VELOCITY);
	}

	/**
	 * Set desired color to a LED of your choice. {@link LedColor#BLACK} turns
	 * the LED off. Setting the color notifies the appropriate ROS publisher to
	 * publish the message in the topic for the LED.
	 * 
	 * @param ledId
	 *            The LED to control.
	 * @param color
	 *            The desired color of the LED.
	 */
	public void setLed(LedId ledId, LedColor color) {
		ledColor.put(ledId, color);
		synchronized (ledId) {
			ledId.notify();
		}
	}

	/**
	 * Play the specified sound.
	 * 
	 * @param sound
	 *            The sound to be played.
	 */
	public void playSound(Sound sound) {
		this.sound = sound;
		synchronized (soundLock) {
			soundLock.notify();
		}
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
