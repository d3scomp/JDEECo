package cz.cuni.mff.d3s.jdeeco.ros;

import geometry_msgs.Point;

import java.util.HashMap;
import java.util.Map;

import kobuki_msgs.BumperEvent;
import kobuki_msgs.WheelDropEvent;
import nav_msgs.Odometry;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Bumper;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.MotorPower;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Wheel;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.WheelState;

/**
 * Provides methods for obtaining sensed values passed through ROS. Subscription
 * to the appropriate ROS topics is handled in the
 * {@link #subscribe(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Sensors extends TopicSubscriber {
	// TODO: document rosjava types that are seen by the user of this interface

	/**
	 * The name of the odometry topic.
	 */
	private static final String ODOMETRY_TOPIC = "odom";
	/**
	 * The last position received in the odometry topic.
	 */
	private Point lastPosition;

	/**
	 * The name of the bumper topic.
	 */
	private static final String BUMPER_TOPIC = "/mobile_base/events/bumper";
	/**
	 * The bumper state.
	 */
	private Bumper bumper;

	/**
	 * The name of the topic to report wheel drop changes.
	 */
	private static final String WHEEL_DROP_TOPIC = "/mobile_base/events/wheel_drop";

	/**
	 * The map of wheel states.
	 */
	private Map<Wheel, WheelState> wheelState;

	/**
	 * The name of the topic for motor power messages.
	 */
	private static final String MOTOR_POWER_TOPIC = "/mobile_base/commands/motor_power";
	
	/**
	 * The power state of robot's motor power.
	 */
	private MotorPower motorPower;
	
	/**
	 * Internal constructor enables the {@link Sensors} to be a singleton.
	 */
	Sensors() {
		bumper = Bumper.RELEASED;
		wheelState = new HashMap<>();
		for (Wheel wheel : Wheel.values()) {
			wheelState.put(wheel, WheelState.RAISED);
		}
		motorPower = MotorPower.ON;
	}

	/**
	 * Register and subscribe to required ROS topics of sensor readings.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	void subscribe(ConnectedNode connectedNode) {
		subscribeOdometry(connectedNode);
		subscribeBumper(connectedNode);
		subscribeWheelDrop(connectedNode);
		subscribeMotorPower(connectedNode);
	}

	/**
	 * Subscribe to the ROS odometry topic.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeOdometry(ConnectedNode connectedNode) {
		Subscriber<Odometry> odometryTopic = connectedNode.newSubscriber(
				ODOMETRY_TOPIC, Odometry._TYPE);
		odometryTopic.addMessageListener(new MessageListener<Odometry>() {
			@Override
			public void onNewMessage(Odometry message) {

				lastPosition = message.getPose().getPose().getPosition();
				/*
				 * System.out.format("Position: %f,%f%n",
				 * message.getPose().getPose().getPosition().getX(),
				 * message.getPose().getPose().getPosition().getY()); // TODO:
				 * logging
				 */
			}
		});
	}

	/**
	 * Subscribe to the ROS bumper topic.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeBumper(ConnectedNode connectedNode) {
		Subscriber<BumperEvent> bumperTopic = connectedNode.newSubscriber(
				BUMPER_TOPIC, BumperEvent._TYPE);
		bumperTopic.addMessageListener(new MessageListener<BumperEvent>() {
			@Override
			public void onNewMessage(BumperEvent message) {
				byte state = message.getState();
				if (state == BumperEvent.RELEASED) {
					bumper = Bumper.RELEASED;
					// System.out.println("released");
				} else {
					bumper = Bumper.fromByte(message.getBumper());

					/*if (message.getBumper() == BumperEvent.LEFT) {
						System.out.println("left");
					}
					if (message.getBumper() == BumperEvent.RIGHT) {
						System.out.println("right");
					}
					if (message.getBumper() == BumperEvent.CENTER) {
						System.out.println("center");
					}*/

				}
				// TODO: log
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
		Subscriber<WheelDropEvent> bumperTopic = connectedNode.newSubscriber(
				WHEEL_DROP_TOPIC, WheelDropEvent._TYPE);
		bumperTopic.addMessageListener(new MessageListener<WheelDropEvent>() {
			@Override
			public void onNewMessage(WheelDropEvent message) {

				Wheel wheel = Wheel.fromByte(message.getWheel());
				WheelState state = WheelState.fromByte(message.getState());
				if (wheel != null && state != null) {
					wheelState.put(wheel, state);
					System.out.println(wheel + " wheel state: " + state);
				}
				// TODO: log
			}
		});
	}
	
	/**
	 * Subscribe to the ROS topic for motor power changes.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeMotorPower(ConnectedNode connectedNode) {
		Subscriber<kobuki_msgs.MotorPower> motorPowerTopic = connectedNode.newSubscriber(
				MOTOR_POWER_TOPIC, kobuki_msgs.MotorPower._TYPE);
		motorPowerTopic.addMessageListener(new MessageListener<kobuki_msgs.MotorPower>() {
			@Override
			public void onNewMessage(kobuki_msgs.MotorPower message) {

				MotorPower parsedMotorPower = MotorPower.fromByte(message.getState());
				if (parsedMotorPower != null) {
					motorPower = parsedMotorPower;
				}
				// TODO: log
			}
		});
	}

	/**
	 * The position published in the odometry topic.
	 * 
	 * @return last value of the position published in the odometry topic.
	 */
	public Point getPosition() {
		return lastPosition;
	}

	/**
	 * The bumper state published in the bumper topic.
	 * 
	 * @return the state of the bumper.
	 */
	public Bumper getBumper() {
		return bumper;
	}

	/**
	 * Get the state of specified wheel of the robot.
	 * 
	 * @param wheel
	 *            The wheel to obtain the state of.
	 * @return The state of the specified wheel.
	 */
	public WheelState getWheelState(Wheel wheel) {
		if (wheelState.containsKey(wheel)) {
			return wheelState.get(wheel);
		}
		return null;
	}
	
	/**
	 * Get the state of robot's motor power.
	 * 
	 * @return the state of motor power.
	 */
	public MotorPower getMotorPower() {
		return motorPower;
	}

}
