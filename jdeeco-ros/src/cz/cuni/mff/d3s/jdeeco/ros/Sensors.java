package cz.cuni.mff.d3s.jdeeco.ros;

import geometry_msgs.Point;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

/**
 * Provides methods for obtaining sensed values passed through ROS. Subscription
 * to the appropriate ROS topics is handled in the
 * {@link #subscribe(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Sensors extends TopicSubscriber {

	/**
	 * The name of the odometry topic.
	 */
	private static final String ODOMETRY_TOPIC = "odom";

	/**
	 * The last position received in the odometry topic.
	 */
	private Point lastPosition;

	/**
	 * The singleton instance of the {@link Sensors} class.
	 */
	private static Sensors INSTANCE;

	/**
	 * Private constructor enables the {@link Sensors} to be a singleton.
	 */
	private Sensors() {
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
		//
		// ODOMETRY topic
		//
		Subscriber<nav_msgs.Odometry> odometryTopic = connectedNode
				.newSubscriber(ODOMETRY_TOPIC, nav_msgs.Odometry._TYPE);
		odometryTopic
				.addMessageListener(new MessageListener<nav_msgs.Odometry>() {
					@Override
					public void onNewMessage(nav_msgs.Odometry message) {

						lastPosition = message.getPose().getPose()
								.getPosition();
						/*
						 * System.out.format("Position: %f,%f%n",
						 * message.getPose().getPose().getPosition().getX(),
						 * message.getPose().getPose().getPosition().getY());\
						 * TODO: logging
						 */

						// TODO: make own implementation of Point to lose
						// dependency on geometry_msgs artifact?
					}
				});
		//
		//
	}

	/**
	 * The position published in the odometry topic.
	 * 
	 * @return last value of the position published in the odometry topic.
	 */
	public Point getPosition() {
		return lastPosition;
	}

}
