package cz.cuni.mff.d3s.jdeeco.ros;

import geometry_msgs.Point;
import geometry_msgs.TransformStamped;

import java.util.HashMap;
import java.util.Map;

import kobuki_msgs.BumperEvent;
import kobuki_msgs.ButtonEvent;
import kobuki_msgs.DockInfraRed;
import kobuki_msgs.WheelDropEvent;
import nav_msgs.Odometry;
import sensor_msgs.NavSatFix;
import sensor_msgs.TimeReference;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.message.MessageListener;
import org.ros.message.Time;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import tf2_msgs.TFMessage;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Bumper;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Button;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.ButtonState;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.DockingIRDiod;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.DockingIRSignal;
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
	private Point odometry;

	/**
	 * The name of the bumper topic.
	 */
	private static final String BUMPER_TOPIC = "/mobile_base/events/bumper";
	/**
	 * The bumper state.
	 */
	private Bumper bumper;

	/**
	 * The name of the topic for button state updates.
	 */
	private static final String BUTTON_TOPIC = "/mobile_base/events/button";

	/**
	 * The state of turtlebot's buttons.
	 */
	private Map<Button, ButtonState> buttonState;

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
	 * The name of the topic for messages from docking diods.
	 */
	private static final String DOCK_IR_TOPIC = "/mobile_base/sensors/dock_ir";

	/**
	 * The signal from docking diods.
	 */
	private Map<DockingIRDiod, DockingIRSignal> dockingIRSignal;
	
	private NavSatFix gpsPosition;
	
	private Time gpsTime;

	/**
	 * Internal constructor enables the {@link Sensors} to be a singleton.
	 */
	Sensors() {
		bumper = Bumper.RELEASED;
		buttonState = new HashMap<>();
		for (Button button : Button.values()) {
			buttonState.put(button, ButtonState.RELEASED);
		}
		wheelState = new HashMap<>();
		for (Wheel wheel : Wheel.values()) {
			wheelState.put(wheel, WheelState.RAISED);
		}
		motorPower = MotorPower.ON;
		dockingIRSignal = new HashMap<>();
		for (DockingIRDiod diod : DockingIRDiod.values()) {
			dockingIRSignal.put(diod, DockingIRSignal.INFINITY);
		}
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
		subscribeButtons(connectedNode);
		subscribeWheelDrop(connectedNode);
		subscribeMotorPower(connectedNode);
		subscribeDockIr(connectedNode);
		subscribeTF(connectedNode);
		subscribeGPS(connectedNode);
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

				odometry = message.getPose().getPose().getPosition();
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

					/*
					 * if (message.getBumper() == BumperEvent.LEFT) {
					 * System.out.println("left"); } if (message.getBumper() ==
					 * BumperEvent.RIGHT) { System.out.println("right"); } if
					 * (message.getBumper() == BumperEvent.CENTER) {
					 * System.out.println("center"); }
					 */

				}
				// TODO: log
			}
		});
	}

	/**
	 * Subscribe to the topic for buttons state updates.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeButtons(ConnectedNode connectedNode) {
		Subscriber<ButtonEvent> buttonTopic = connectedNode.newSubscriber(
				BUTTON_TOPIC, ButtonEvent._TYPE);
		buttonTopic.addMessageListener(new MessageListener<ButtonEvent>() {
			@Override
			public void onNewMessage(ButtonEvent message) {
				Button button = Button.fromByte(message.getButton());
				ButtonState state = ButtonState.fromByte(message.getState());
				if (button != null && state != null) {
					buttonState.put(button, state);
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
					//System.out.println(wheel + " wheel state: " + state);
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
		Subscriber<kobuki_msgs.MotorPower> motorPowerTopic = connectedNode
				.newSubscriber(MOTOR_POWER_TOPIC, kobuki_msgs.MotorPower._TYPE);
		motorPowerTopic
				.addMessageListener(new MessageListener<kobuki_msgs.MotorPower>() {
					@Override
					public void onNewMessage(kobuki_msgs.MotorPower message) {

						MotorPower parsedMotorPower = MotorPower
								.fromByte(message.getState());
						if (parsedMotorPower != null) {
							motorPower = parsedMotorPower;
						}
						// TODO: log
					}
				});
	}

	/**
	 * Subscribe to the topic for docking infra-red diod signals.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeDockIr(ConnectedNode connectedNode) {
		Subscriber<DockInfraRed> dockIrTopic = connectedNode.newSubscriber(
				DOCK_IR_TOPIC, DockInfraRed._TYPE);
		dockIrTopic.addMessageListener(new MessageListener<DockInfraRed>() {
			@Override
			public void onNewMessage(DockInfraRed message) {

				/*
				 * System.out.println(String.format(
				 * "NL: %d\nNC: %d\nNR: %d\nFL: %d\nFC: %d\nFR: %d",
				 * DockInfraRed.NEAR_LEFT, DockInfraRed.NEAR_CENTER,
				 * DockInfraRed.NEAR_RIGHT, DockInfraRed.FAR_LEFT,
				 * DockInfraRed.FAR_CENTER, DockInfraRed.FAR_RIGHT));
				 */
				ChannelBuffer b = message.getData();

				for (int i = 0; i < 3; i++) {
					dockingIRSignal.put(DockingIRDiod.fromIndex(i),
							DockingIRSignal.fromByte(b.getByte(i)));

				}

				/*System.out.println("R: " + b.getByte(0));
				System.out.println("C: " + b.getByte(1));
				System.out.println("L: " + b.getByte(2));*/
				// TODO: log
			}
		});
	}


	private void subscribeTF(ConnectedNode connectedNode) {
		Subscriber<NavSatFix> navSatTopic = connectedNode.newSubscriber(
				"/gps/position", NavSatFix._TYPE);
		navSatTopic.addMessageListener(new MessageListener<NavSatFix>() {
			@Override
			public void onNewMessage(NavSatFix message) {
				gpsPosition = message;
			}
		});
		
		Subscriber<TimeReference> timeRefTopic = connectedNode.newSubscriber(
				"/gps/position", TimeReference._TYPE);
		timeRefTopic.addMessageListener(new MessageListener<TimeReference>() {
			@Override
			public void onNewMessage(TimeReference message) {
				gpsTime = message.getTimeRef();
				
			}
		});
	}
	

	private void subscribeGPS(ConnectedNode connectedNode) {
		Subscriber<TFMessage> transformTopic = connectedNode.newSubscriber(
				"/tf", TFMessage._TYPE);
		transformTopic.addMessageListener(new MessageListener<TFMessage>() {
			@Override
			public void onNewMessage(TFMessage message) {

				/*System.out.println("transform:");
				for(TransformStamped tfs : message.getTransforms()){
					System.out.println(tfs.getChildFrameId());
					System.out.println(String.format("[%f, %f, %f]",
							tfs.getTransform().getTranslation().getX(),
							tfs.getTransform().getTranslation().getY(),
							tfs.getTransform().getTranslation().getZ()));
					System.out.println(String.format("[%f, %f, %f, %f]",
							tfs.getTransform().getRotation().getX(),
							tfs.getTransform().getRotation().getY(),
							tfs.getTransform().getRotation().getZ(),
							tfs.getTransform().getRotation().getW()));
				}
				System.out.println();*/
			}
		});
	}
	
	/**
	 * The position published in the odometry topic.
	 * 
	 * @return last value of the position published in the odometry topic.
	 */
	public Point getOdometry() {
		return odometry;
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
	 * Get the state of the specified button.
	 * 
	 * @param button
	 *            The required button to get the state of.
	 * @return The state of the specified button. Null if the specified button
	 *         is not valid.
	 */
	public ButtonState getButtonState(Button button) {
		if (buttonState.containsKey(button)) {
			return buttonState.get(button);
		}
		return null;
	}

	/**
	 * Get the state of specified wheel of the robot.
	 * 
	 * @param wheel
	 *            The wheel to obtain the state of.
	 * @return The state of the specified wheel. Null If the specified wheel is
	 *         not valid.
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

	public NavSatFix getGpsPosition(){
		return gpsPosition;
	}
	
	public long getGpsTime(){
		return gpsTime.totalNsecs() * 1000;
	}
	
	/**
	 * Get the signal from the specified infra-red docking diod.
	 * 
	 * @param diod
	 *            The diod to get the signal from.
	 * @return The signal from the specified infra-red docking diod. Null if the
	 *         specified diod is not valid.
	 */
	public DockingIRSignal getDockingIRSignal(DockingIRDiod diod) {
		if (dockingIRSignal.containsKey(diod)) {
			return dockingIRSignal.get(diod);
		}
		return null;
	}

}
