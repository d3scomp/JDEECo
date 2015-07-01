package cz.cuni.mff.d3s.jdeeco.ros;

import geometry_msgs.Point;
import nav_msgs.Odometry;

import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.message.Time;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import sensor_msgs.NavSatFix;
import sensor_msgs.TimeReference;
import tf2_msgs.TFMessage;

/**
 * Provides methods for obtaining sensed values passed through ROS. Subscription
 * to the appropriate ROS topics is handled in the
 * {@link #subscribe(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Position extends TopicSubscriber {
	// TODO: document rosjava types that are seen by the user of this interface

	/**
	 * The name of the topic for odometry resets.
	 */
	private static final String RESET_ODOMETRY_TOPIC = "/mobile_base/commands/reset_odometry";
	/**
	 * The name of the odometry topic.
	 */
	private static final String ODOMETRY_TOPIC = "/odom";
	/**
	 * The name of the GPS position topic.
	 */
	private static final String GPS_POSITION_TOPIC = "/gps/position";
	/**
	 * The name of the GPS time topic.
	 */
	private static final String GPS_TIME_TOPIC = "/gps/time";
	/**
	 * The name of the transformation topic.
	 */
	private static final String TRANSFORMATION_TOPIC = "/tf";

	/**
	 * The last position received in the odometry topic.
	 */
	private Point odometry;
	/**
	 * The lock to wait and notify on when a odometry reset is requested.
	 */
	private final Object resetOdometryLock;

	/**
	 * The last received position measured by GPS.
	 */
	private NavSatFix gpsPosition;
	/**
	 * The last time measured by the GPS sensor.
	 */
	private Time gpsTime;

	/**
	 * Internal constructor enables the {@link RosServices} to be in the control
	 * of instantiating {@link Position}.
	 */
	Position() {
		resetOdometryLock = new Object();
	}

	/**
	 * Register and subscribe to required ROS topics of sensor readings.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	protected void subscribeDescendant(ConnectedNode connectedNode) {
		subscribeOdometry(connectedNode);
		subscribeTF(connectedNode);
		subscribeGPS(connectedNode);
	}

	/**
	 * Subscribe to the ROS odometry topic and for the odometry reset messages.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeOdometry(ConnectedNode connectedNode) {
		// Subscribe to listen on odometry messages
		Subscriber<Odometry> odometryTopic = connectedNode.newSubscriber(
				ODOMETRY_TOPIC, Odometry._TYPE);
		odometryTopic.addMessageListener(new MessageListener<Odometry>() {
			@Override
			public void onNewMessage(Odometry message) {
				odometry = message.getPose().getPose().getPosition();
				// TODO: logging
			}
		});

		// Subscribe to publish odometry reset messages
		Publisher<std_msgs.Empty> resetOdometryTopic = connectedNode
				.newPublisher(RESET_ODOMETRY_TOPIC, std_msgs.Empty._TYPE);
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			@Override
			protected void setup() {
			}

			@Override
			protected void loop() throws InterruptedException {
				synchronized (resetOdometryLock) {
					resetOdometryLock.wait();
				}

				std_msgs.Empty emptyMsg = resetOdometryTopic.newMessage();
				resetOdometryTopic.publish(emptyMsg);
				// TODO: log
			}
		});
	}

	/**
	 * Subscribe for the GPS messages.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeGPS(ConnectedNode connectedNode) {
		// Subscribe to the GPS position topic
		Subscriber<NavSatFix> navSatTopic = connectedNode.newSubscriber(
				GPS_POSITION_TOPIC, NavSatFix._TYPE);
		navSatTopic.addMessageListener(new MessageListener<NavSatFix>() {
			@Override
			public void onNewMessage(NavSatFix message) {
				gpsPosition = message;
			}
		});

		// Subscribe to the GPS time topic
		Subscriber<TimeReference> timeRefTopic = connectedNode.newSubscriber(
				GPS_TIME_TOPIC, TimeReference._TYPE);
		timeRefTopic.addMessageListener(new MessageListener<TimeReference>() {
			@Override
			public void onNewMessage(TimeReference message) {
				gpsTime = message.getTimeRef();

			}
		});
	}

	/**
	 * Subscribe for the transformation messages.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeTF(ConnectedNode connectedNode) {
		Subscriber<TFMessage> transformTopic = connectedNode.newSubscriber(
				TRANSFORMATION_TOPIC, TFMessage._TYPE);
		transformTopic.addMessageListener(new MessageListener<TFMessage>() {
			@Override
			public void onNewMessage(TFMessage message) {
				// TODO: Build TF tree

				/*
				 * System.out.println("transform:"); for(TransformStamped tfs :
				 * message.getTransforms()){
				 * System.out.println(tfs.getChildFrameId());
				 * System.out.println(String.format("[%f, %f, %f]",
				 * tfs.getTransform().getTranslation().getX(),
				 * tfs.getTransform().getTranslation().getY(),
				 * tfs.getTransform().getTranslation().getZ()));
				 * System.out.println(String.format("[%f, %f, %f, %f]",
				 * tfs.getTransform().getRotation().getX(),
				 * tfs.getTransform().getRotation().getY(),
				 * tfs.getTransform().getRotation().getZ(),
				 * tfs.getTransform().getRotation().getW())); }
				 * System.out.println();
				 */
			}
		});
	}

	/**
	 * Reset the odometry counter.
	 */
	public void resetOdometry() {
		synchronized (resetOdometryLock) {
			resetOdometryLock.notify();
		}
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
	 * Read the GPS position.
	 * 
	 * @return The position measured by GPS.
	 */
	public NavSatFix getGpsPosition() {
		return gpsPosition;
	}

	/**
	 * Get the time read by the GPS sensor.
	 * 
	 * @return The time measured by GPS.
	 */
	public long getGpsTime() {
		return gpsTime.totalNsecs() * 1000;
	}

}
