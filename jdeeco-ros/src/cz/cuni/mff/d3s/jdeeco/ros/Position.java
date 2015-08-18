package cz.cuni.mff.d3s.jdeeco.ros;

import geometry_msgs.Pose;
import geometry_msgs.PoseStamped;
import geometry_msgs.PoseWithCovariance;
import geometry_msgs.PoseWithCovarianceStamped;
import nav_msgs.Odometry;

import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.message.Time;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Point;
import cz.cuni.mff.d3s.deeco.logging.Log;
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
	 * A switch that guards the subscription to "tf" (transformations) topic. If
	 * true then the {@link Position} object is subscribed to the "tf" topic.
	 */
	private static final boolean ENABLE_TF_LISTENER = false;

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
	 * The name of the <a href="http://wiki.ros.org/amcl">AMCL</a> positioning
	 * topic.
	 */
	private static final String AMCL_POSITION_TOPIC = "/amcl_pose";
	/**
	 * The name of the topic for simple goals for base movement.
	 */
	private static final String SIMPLE_GOAL_TOPIC = "/move_base_simple/goal";
	/**
	 * The name of the transformation topic.
	 */
	private static final String TRANSFORMATION_TOPIC = "/tf";

	/**
	 * The frame in which the goal coordinates are given.
	 */
	private static final String MAP_FRAME = "map";

	/**
	 * The odometry topic.
	 */
	private Subscriber<Odometry> odometryTopic = null;
	/**
	 * The topic for odometry resets.
	 */
	private Publisher<std_msgs.Empty> resetOdometryTopic = null;
	/**
	 * The GPS position topic.
	 */
	private Subscriber<NavSatFix> navSatTopic = null;
	/**
	 * The GPS time topic.
	 */
	private Subscriber<TimeReference> timeRefTopic = null;
	/**
	 * The <a href="http://wiki.ros.org/amcl">AMCL</a> positioning topic.
	 */
	private Subscriber<PoseWithCovarianceStamped> amclTopic = null;
	/**
	 * The topic for simple goals for base movement.
	 */
	private Publisher<PoseStamped> goalTopic = null;
	/**
	 * The transformation topic.
	 */
	private Subscriber<TFMessage> transformTopic = null;

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
	 * The most probable position of the robot based on the
	 * <a href="http://wiki.ros.org/amcl">AMCL</a> positioning algorithm.
	 */
	private PoseWithCovariance amclPosition;
	/**
	 * The target position for the robot.
	 */
	private Pose simpleGoal;
	/**
	 * The lock to wait and notify on when a simple goal is set.
	 */
	private final Object simpleGoalLock;

	/**
	 * Create a new instance of {@link Position}.
	 */
	public Position() {
		resetOdometryLock = new Object();
		simpleGoalLock = new Object();
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
		subscribeGPS(connectedNode);
		subscribeAMCL(connectedNode);
		subscribeSimpleGoal(connectedNode);

		if (ENABLE_TF_LISTENER) {
			subscribeTF(connectedNode);
		}
	}

	/**
	 * Finalize the connection to ROS topics.
	 * 
	 * @param node
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	void unsubscribe(Node node) {
		if (odometryTopic != null) {
			odometryTopic.shutdown();
		}
		if (resetOdometryTopic != null) {
			resetOdometryTopic.shutdown();
		}
		if (navSatTopic != null) {
			navSatTopic.shutdown();
		}
		if (timeRefTopic != null) {
			timeRefTopic.shutdown();
		}
		if (amclTopic != null) {
			amclTopic.shutdown();
		}
		if (goalTopic != null) {
			goalTopic.shutdown();
		}
		if (transformTopic != null) {
			transformTopic.shutdown();
		}
	}

	/**
	 * Subscribe to the ROS odometry topic and for the odometry reset messages.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeOdometry(ConnectedNode connectedNode) {
		// Subscribe to listen on odometry messages
		odometryTopic = connectedNode.newSubscriber(ODOMETRY_TOPIC, Odometry._TYPE);
		odometryTopic.addMessageListener(new MessageListener<Odometry>() {
			@Override
			public void onNewMessage(Odometry message) {
				odometry = pointFromMessage(message.getPose().getPose().getPosition());

				Log.d(String.format("Odometry value received: [%f, %f, %f].",
						odometry.x, odometry.y, odometry.z));
			}
		});

		// Subscribe to publish odometry reset messages
		resetOdometryTopic = connectedNode.newPublisher(RESET_ODOMETRY_TOPIC, std_msgs.Empty._TYPE);
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
				Log.d("Odometry reset initiated.");
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
		navSatTopic = connectedNode.newSubscriber(GPS_POSITION_TOPIC, NavSatFix._TYPE);
		navSatTopic.addMessageListener(new MessageListener<NavSatFix>() {
			@Override
			public void onNewMessage(NavSatFix message) {
				gpsPosition = message;

				Log.d(String.format("GPS possition received: [%d, %d, %d].", message.getLatitude(),
						message.getLongitude(), message.getAltitude()));
			}
		});

		// Subscribe to the GPS time topic
		timeRefTopic = connectedNode.newSubscriber(GPS_TIME_TOPIC, TimeReference._TYPE);
		timeRefTopic.addMessageListener(new MessageListener<TimeReference>() {
			@Override
			public void onNewMessage(TimeReference message) {
				gpsTime = message.getTimeRef();

				Log.d(String.format("GPS time received: %d.", gpsTime.secs));
			}
		});
	}

	/**
	 * Subscribe for the AMCL positioning messages.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeAMCL(ConnectedNode connectedNode) {
		amclTopic = connectedNode.newSubscriber(AMCL_POSITION_TOPIC, PoseWithCovarianceStamped._TYPE);
		amclTopic.addMessageListener(new MessageListener<PoseWithCovarianceStamped>() {
			@Override
			public void onNewMessage(PoseWithCovarianceStamped message) {
				amclPosition = message.getPose();

				Log.d(String.format("AMCL position received: [%f, %f, %f] with orientation [%f, %f, %f, %f].",
						amclPosition.getPose().getPosition().getX(), amclPosition.getPose().getPosition().getY(),
						amclPosition.getPose().getPosition().getZ(), amclPosition.getPose().getOrientation().getX(),
						amclPosition.getPose().getOrientation().getY(), amclPosition.getPose().getOrientation().getZ(),
						amclPosition.getPose().getOrientation().getW()));
			}
		});
	}

	/**
	 * Subscribe to the topic for simple goals.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeSimpleGoal(ConnectedNode connectedNode) {
		goalTopic = connectedNode.newPublisher(SIMPLE_GOAL_TOPIC, PoseStamped._TYPE);
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			@Override
			protected void setup() {
				simpleGoal = goalTopic.newMessage().getPose();
			}

			@Override
			protected void loop() throws InterruptedException {
				synchronized (simpleGoalLock) {
					simpleGoalLock.wait();
				}

				PoseStamped goalMsg = goalTopic.newMessage();
				goalMsg.setPose(simpleGoal);
				goalMsg.getHeader().setFrameId(MAP_FRAME);
				goalMsg.getHeader().setStamp(connectedNode.getCurrentTime());
				goalTopic.publish(goalMsg);

				Log.d(String.format("Simple goal set to position: [%f, %f, %f] with orientation [%f, %f, %f, %f].",
						simpleGoal.getPosition().getX(), simpleGoal.getPosition().getY(),
						simpleGoal.getPosition().getZ(), simpleGoal.getOrientation().getX(),
						simpleGoal.getOrientation().getY(), simpleGoal.getOrientation().getZ(),
						simpleGoal.getOrientation().getW()));
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
		transformTopic = connectedNode.newSubscriber(TRANSFORMATION_TOPIC, TFMessage._TYPE);
		transformTopic.addMessageListener(new MessageListener<TFMessage>() {
			@Override
			public void onNewMessage(TFMessage message) {
				// TODO: Build TF tree

				Log.d("TF frame received.");
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
		return gpsTime != null ? gpsTime.totalNsecs() * 1000 : 0;
	}

	/**
	 * Probabilistic position of the robot computed by the
	 * <a href="http://wiki.ros.org/amcl">AMCL</a> algorithm.
	 * 
	 * @return The probabilistic position of the robot.
	 */
	public PoseWithCovariance getPosition() {
		return amclPosition;
	}

	/**
	 * Set a goal for the "move base" service. After the goal is set the robot
	 * starts to move to reach it. The goal consist of a position and an
	 * orientation. The position is expressed by three cartesian coordinates (X,
	 * Y, Z). The orientation is expressed by quaternion (X, Y, Z, W).
	 * 
	 * @param posX
	 *            The X coordinate for position.
	 * @param posY
	 *            The Y coordinate for position.
	 * @param posZ
	 *            The Z coordinate for position.
	 * @param oriX
	 *            The X coordinate for orientation.
	 * @param oriY
	 *            The Y coordinate for orientation.
	 * @param oriZ
	 *            The Z coordinate for orientation.
	 * @param oriW
	 *            The W coordinate for orientation.
	 */
	public void setSimpleGoal(double posX, double posY, double posZ, double oriX, double oriY, double oriZ,
			double oriW) {
		simpleGoal.getPosition().setX(posX);
		simpleGoal.getPosition().setY(posY);
		simpleGoal.getPosition().setZ(posZ);
		simpleGoal.getOrientation().setX(oriX);
		simpleGoal.getOrientation().setY(oriY);
		simpleGoal.getOrientation().setZ(oriZ);
		simpleGoal.getOrientation().setW(oriW);

		synchronized (simpleGoalLock) {
			simpleGoalLock.notify();
		}
	}

	/**
	 * Transform given {@link geometry_msgs.Point} into {@link Point} instance.
	 * 
	 * @param point
	 *            The {@link geometry_msgs.Point} to be transformed.
	 * @return A {@link Point} with the values taken from the given point
	 *         argument.
	 */
	private Point pointFromMessage(geometry_msgs.Point point) {
		return new Point(point.getX(), point.getY(), point.getZ());
	}

}
