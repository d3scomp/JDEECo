package cz.cuni.mff.d3s.jdeeco.ros;

import geometry_msgs.Twist;

import java.util.HashMap;
import java.util.Map;

import kobuki_msgs.WheelDropEvent;

import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.MotorPower;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.WheelID;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.WheelState;

/**
 * Provides methods to command wheel actuators and to obtain data from wheel
 * sensors through ROS. Registration of appropriate ROS topics is handled in the
 * {@link #subscribe(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Wheels extends TopicSubscriber {

	/**
	 * The name of the velocity topic.
	 */
	private static final String VELOCITY_TOPIC = "/mobile_base/commands/velocity";
	/**
	 * The name of the topic for motor power messages.
	 */
	private static final String MOTOR_POWER_TOPIC = "/mobile_base/commands/motor_power";
	/**
	 * The name of the topic to report wheel drop changes.
	 */
	private static final String WHEEL_DROP_TOPIC = "/mobile_base/events/wheel_drop";

	/**
	 * The velocity topic.
	 */
	private Publisher<Twist> velocityTopic = null;
	/**
	 * The topic for motor power messages.
	 */
	 private Publisher<kobuki_msgs.MotorPower> motorPowerTopicPub = null;
	/**
	 * The topic to report wheel drop changes.
	 */
	 private Subscriber<WheelDropEvent> wheelDropTopic = null;

	/**
	 * The maximum absolute linear speed. Value taken from kobuki_keyop.
	 */
	private static final double MAX_LINEAR_VELOCITY = 1.5;
	/**
	 * The maximum absolute angular speed. Value taken from kobuki_keyop.
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
	 * The lock to wait and notify on when a velocity should be set.
	 */
	private final Object velocityLock;

	/**
	 * The state of motor power to be set.
	 */
	private MotorPower motorPower;
	/**
	 * The lock to wait and notify on when a motor power should be set.
	 */
	private final Object motorPowerLock;

	/**
	 * The map of wheel states.
	 */
	private Map<WheelID, WheelState> wheelState;

	/**
	 * Internal constructor enables the {@link RosServices} to be in the control
	 * of instantiating {@link Wheels}.
	 */
	Wheels() {
		velocityLock = new Object();
		motorPowerLock = new Object();

		wheelState = new HashMap<>();
		for (WheelID wheel : WheelID.values()) {
			wheelState.put(wheel, WheelState.RAISED);
		}
		motorPower = MotorPower.ON;
	}

	/**
	 * Register and subscribe to required ROS topics for actuator commands.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	protected void subscribeDescendant(ConnectedNode connectedNode) {
		subscribeVelocity(connectedNode);
		subscribeMotorPower(connectedNode);
		subscribeWheelDrop(connectedNode);
	}

	/**
	 * Finalize the connection to ROS topics.
	 * 
	 * @param node
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	void unsubscribe(Node node) {
		if(velocityTopic != null){
			velocityTopic.shutdown();
		}
		if(motorPowerTopicPub != null){
			motorPowerTopicPub.shutdown();
		}
		if(wheelDropTopic != null){
			wheelDropTopic.shutdown();
		}
	}

	/**
	 * Subscribe to the ROS topic for velocity.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeVelocity(ConnectedNode connectedNode) {
		velocityTopic = connectedNode.newPublisher(
				VELOCITY_TOPIC, Twist._TYPE);
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			@Override
			protected void setup() {
				linearVelocity = 0;
				angularVelocity = 0;
			}

			@Override
			protected void loop() throws InterruptedException {
				synchronized (velocityLock) {
					// Wait until the velocity is set
					velocityLock.wait();
				}
				
				Twist twist = velocityTopic.newMessage();
				// Set the linear velocity (X axis)
				twist.getLinear().setX(linearVelocity);
				twist.getLinear().setY(0);
				twist.getLinear().setZ(0);
				// Set the angular velocity (Z axis)
				twist.getAngular().setX(0);
				twist.getAngular().setY(0);
				twist.getAngular().setZ(angularVelocity);
				
				velocityTopic.publish(twist);

				Log.d(String.format("Velocity set to: %f with yaw: %f.",
						linearVelocity, angularVelocity));
			}
		});
	}

	/**
	 * Subscribe to the ROS topic for motor power messages. To publish motor
	 * power message wait until notified by the
	 * {@link #setMotorPower(MotorPower)} setter.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeMotorPower(ConnectedNode connectedNode) {
		// Subscribe to publish motor power changes
		motorPowerTopicPub = connectedNode
				.newPublisher(MOTOR_POWER_TOPIC, kobuki_msgs.MotorPower._TYPE);
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			@Override
			protected void setup() {
				motorPower = MotorPower.ON;
			}

			@Override
			protected void loop() throws InterruptedException {
				synchronized (motorPowerLock) {
					// Wait until the motor power is set
					motorPowerLock.wait();
				}

				kobuki_msgs.MotorPower motorPowerMsg = motorPowerTopicPub
						.newMessage();
				motorPowerMsg.setState(motorPower.value);
				motorPowerTopicPub.publish(motorPowerMsg);

				Log.d(String.format("Motor power set to: %d.", motorPower.value));
			}
		});
		
		// Subscribe to listen on motor power changes
		Subscriber<kobuki_msgs.MotorPower> motorPowerTopicSub = connectedNode
				.newSubscriber(MOTOR_POWER_TOPIC, kobuki_msgs.MotorPower._TYPE);
		motorPowerTopicSub
				.addMessageListener(new MessageListener<kobuki_msgs.MotorPower>() {
					@Override
					public void onNewMessage(kobuki_msgs.MotorPower message) {

						MotorPower parsedMotorPower = MotorPower
								.fromByte(message.getState());
						if (parsedMotorPower != null) {
							motorPower = parsedMotorPower;
						}

						Log.d(String.format("Motor power change received: %d.",
								message.getState()));
					}
				});
	}
	
	/**
	 * Subscribe to the ROS topic for wheel drop changes.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeWheelDrop(ConnectedNode connectedNode) {
		wheelDropTopic = connectedNode.newSubscriber(
				WHEEL_DROP_TOPIC, WheelDropEvent._TYPE);
		wheelDropTopic.addMessageListener(new MessageListener<WheelDropEvent>() {
			@Override
			public void onNewMessage(WheelDropEvent message) {

				WheelID wheel = WheelID.fromByte(message.getWheel());
				WheelState state = WheelState.fromByte(message.getState());
				if (wheel != null && state != null) {
					wheelState.put(wheel, state);
				}

				Log.d(String.format("Wheel drop change received: %d on wheel: %d.",
						message.getState(), message.getWheel()));
			}
		});
	}

	/**
	 * Set the velocity. Use normalized values from -1 to 1. The sign determines
	 * direction and the value percentage of motor power. If a value outside of
	 * the range is provided it is used the nearest bound value.
	 * 
	 * <p>The value has to be set periodically to keep the robot moving. If the
	 * robot doesn't receive velocity update for longer than 600 ms it stops
	 * moving.</p>
	 * 
	 * @param linear
	 *            The linear velocity. Value in [-1, 1]. Negative value moves
	 *            the robot backwards. Positive value moves the robot forwards.
	 * @param angular
	 *            The angular velocity. Value in [-1, 1]. Negative value turns
	 *            the robot right. Positive value turns the robot left.
	 */
	public void setVelocity(double linear, double angular) {
		if (linear < -1) {
			linear = -1;
		}
		if (linear > 1) {
			linear = 1;
		}
		if (angular < -1) {
			angular = -1;
		}
		if (angular > 1) {
			angular = 1;
		}

		linearVelocity = fromNormalized(linear, MAX_LINEAR_VELOCITY);
		angularVelocity = fromNormalized(angular, MAX_ANGULAR_VELOCITY);

		synchronized (velocityLock) {
			velocityLock.notify();
		}
	}

	/**
	 * Enable or disable robot's motors.
	 * 
	 * @param motorPower
	 *            The motor power state to be set.
	 */
	public void setMotorPower(MotorPower motorPower) {
		this.motorPower = motorPower;
		synchronized (motorPowerLock) {
			motorPowerLock.notify();
		}
	}

	/**
	 * Get the state of robot's motor power.
	 * 
	 * @return the state of motor power.
	 */
	public MotorPower getMotorPower() {
		return motorPower;
	}
	
	/**
	 * Get the state of specified wheel of the robot.
	 * 
	 * @param wheel
	 *            The wheel to obtain the state of.
	 * @return The state of the specified wheel. Null If the specified wheel is
	 *         not valid.
	 */
	public WheelState getWheelState(WheelID wheel) {
		if (wheelState.containsKey(wheel)) {
			return wheelState.get(wheel);
		}
		return null;
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
