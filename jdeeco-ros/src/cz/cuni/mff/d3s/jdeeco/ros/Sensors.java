package cz.cuni.mff.d3s.jdeeco.ros;

import geometry_msgs.Point;
import kobuki_msgs.BumperEvent;
import nav_msgs.Odometry;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Bumper;

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
	 * The singleton instance of the {@link Sensors} class.
	 */
	private static Sensors INSTANCE;

	/**
	 * Private constructor enables the {@link Sensors} to be a singleton.
	 */
	private Sensors() {
		bumper = Bumper.RELEASED;
	}

	/**
	 * Provides the singleton instance of the {@link Sensors}.
	 * 
	 * @return the singleton instance of the {@link Sensors}.
	 */
	public static Sensors getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Sensors();
		}
		return INSTANCE;
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

}
