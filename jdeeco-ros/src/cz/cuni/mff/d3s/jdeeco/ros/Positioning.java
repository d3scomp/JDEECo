package cz.cuni.mff.d3s.jdeeco.ros;

import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.message.Time;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.jdeeco.position.Position;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.position.PositionProvider;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.GpsData;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.MoveResult;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Orientation;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.PoseWithCovariance;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.ROSPosition;
import geometry_msgs.Pose;
import geometry_msgs.PoseStamped;
import geometry_msgs.PoseWithCovarianceStamped;
import move_base_msgs.MoveBaseActionResult;
import nav_msgs.Odometry;
import sensor_msgs.NavSatFix;
import sensor_msgs.TimeReference;
import tf2_msgs.TFMessage;

/**
 * Provides methods for obtaining sensed values passed through ROS. Subscription to the appropriate ROS topics is
 * handled in the {@link #subscribe(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Positioning extends TopicSubscriber implements PositionProvider {
	/**
	 * A switch that guards the subscription to "tf" (transformations) topic. If true then the {@link Positioning}
	 * object is subscribed to the "tf" topic.
	 */
	private static final boolean ENABLE_TF_LISTENER = false;

	/**
	 * The name of the topic for odometry resets.
	 */
	private static final String RESET_ODOMETRY_TOPIC = "mobile_base/commands/reset_odometry";
	/**
	 * The name of the odometry topic.
	 */
	private static final String ODOMETRY_TOPIC = "odom";
	/**
	 * The name of the GPS position topic.
	 */
	private static final String GPS_POSITION_TOPIC = "gps/position";
	/**
	 * The name of the GPS time topic.
	 */
	private static final String GPS_TIME_TOPIC = "gps/time";
	/**
	 * The name of the <a href="http://wiki.ros.org/amcl">AMCL</a> positioning topic.
	 */
	private static final String AMCL_POSITION_TOPIC = "amcl_pose";
	/**
	 * The name of the topic for simple goals for base movement.
	 */
	private static final String SIMPLE_GOAL_TOPIC = "move_base_simple/goal";
	/**
	 * The name of the topic for move_base result
	 */
	private static final String MOVE_BASE_RESULT_TOPIC = "move_base/result";
	/**
	 * The name of the transformation topic.
	 */
	private static final String TRANSFORMATION_TOPIC = "tf";

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
	 * The topic for simple goals for base movement.
	 */
	private Subscriber<MoveBaseActionResult> moveBaseResultTopic = null;
	/**
	 * The transformation topic.
	 */
	private Subscriber<TFMessage> transformTopic = null;

	/**
	 * The last position received in the odometry topic.
	 */
	private ROSPosition odometry;
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
	 * The most probable position of the robot based on the <a href="http://wiki.ros.org/amcl">AMCL</a> positioning
	 * algorithm.
	 */
	private PoseWithCovariance amclPosition;
	/**
	 * The target position for the robot.
	 */
	private Pose simpleGoal;
	/**
	 * Move base result
	 */
	private MoveResult moveBaseResult;
	/**
	 * The lock to wait and notify on when a simple goal is set.
	 */
	private final Object simpleGoalLock;

	/**
	 * Create a new instance of {@link Positioning}.
	 */
	public Positioning() {
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
		subscribeMoveBaseResult(connectedNode);

		if (ENABLE_TF_LISTENER) {
			subscribeTF(connectedNode);
		}
		
		// Try to register as position provider and obtain initial position
		PositionPlugin positionPlugin = this.container.getPluginInstance(PositionPlugin.class);
		if(positionPlugin != null) {
			// Grab initial position
			amclPosition = new PoseWithCovariance(
					ROSPosition.fromPosition(positionPlugin.getStaticPosition()),
					new Orientation(0, 0, 0, 1),
					new double[]{1});
			// Register as position provider 
			positionPlugin.setProvider(this);
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
		odometryTopic = connectedNode.newSubscriber(rosServices.getNamespace() + ODOMETRY_TOPIC, Odometry._TYPE);
		odometryTopic.addMessageListener(new MessageListener<Odometry>() {
			@Override
			public void onNewMessage(Odometry message) {
				odometry = pointFromMessage(message.getPose().getPose().getPosition());

				Log.d(String.format("Odometry value received: [%f, %f, %f].", odometry.x, odometry.y, odometry.z));
			}
		});

		// Subscribe to publish odometry reset messages
		resetOdometryTopic = connectedNode.newPublisher(rosServices.getNamespace() + RESET_ODOMETRY_TOPIC,
				std_msgs.Empty._TYPE);
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
		navSatTopic = connectedNode.newSubscriber(rosServices.getNamespace() + GPS_POSITION_TOPIC, NavSatFix._TYPE);
		navSatTopic.addMessageListener(new MessageListener<NavSatFix>() {
			@Override
			public void onNewMessage(NavSatFix message) {
				gpsPosition = message;

				Log.d(String.format("GPS possition received: [%d, %d, %d].", message.getLatitude(),
						message.getLongitude(), message.getAltitude()));
			}
		});

		// Subscribe to the GPS time topic
		timeRefTopic = connectedNode.newSubscriber(rosServices.getNamespace() + GPS_TIME_TOPIC, TimeReference._TYPE);
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
		amclTopic = connectedNode.newSubscriber(rosServices.getNamespace() + AMCL_POSITION_TOPIC,
				PoseWithCovarianceStamped._TYPE);
		amclTopic.addMessageListener(new MessageListener<PoseWithCovarianceStamped>() {
			@Override
			public void onNewMessage(PoseWithCovarianceStamped message) {
				amclPosition = poseFromMessage(message.getPose());

				Log.d(String.format("AMCL position received: [%f, %f, %f] with orientation [%f, %f, %f, %f].",
						amclPosition.position.x, amclPosition.position.y, amclPosition.position.z,
						amclPosition.orientation.x, amclPosition.orientation.y, amclPosition.orientation.z,
						amclPosition.orientation.w));
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
		goalTopic = connectedNode.newPublisher(rosServices.getNamespace() + SIMPLE_GOAL_TOPIC, PoseStamped._TYPE);
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
	 * Subscribe for the move_base result messages.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	private void subscribeMoveBaseResult(ConnectedNode connectedNode) {
		moveBaseResultTopic = connectedNode.newSubscriber(rosServices.getNamespace() + MOVE_BASE_RESULT_TOPIC,
				MoveBaseActionResult._TYPE);
		moveBaseResultTopic.addMessageListener(new MessageListener<MoveBaseActionResult>() {
			@Override
			public void onNewMessage(MoveBaseActionResult message) {
				moveBaseResult = MoveResult.fromMoveBaseActionResult(message);

				Log.d(String.format("MoveBaseResult received: [%s]", moveBaseResult.toString()));
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
		transformTopic = connectedNode.newSubscriber(rosServices.getNamespace() + TRANSFORMATION_TOPIC,
				TFMessage._TYPE);
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
	public ROSPosition getOdometry() {
		return odometry;
	}

	/**
	 * Read the GPS position and time.
	 * 
	 * @return The position and time measured by the GPS module.
	 */
	public GpsData getGpsData() {
		long msTime = gpsTime != null ? gpsTime.totalNsecs() * 1000 : 0;
		double lat, lon, alt;
		if (gpsPosition != null) {
			lat = gpsPosition.getLatitude();
			lon = gpsPosition.getLongitude();
			alt = gpsPosition.getAltitude();
		} else {
			lat = 0;
			lon = 0;
			alt = 0;
		}
		return new GpsData(lat, lon, alt, msTime);
	}

	/**
	 * Probabilistic position with covariance of the robot computed by the <a href="http://wiki.ros.org/amcl">AMCL</a>
	 * algorithm as provided by ROS.
	 * 
	 * @return The probabilistic position of the robot.
	 */
	public PoseWithCovariance getPoseWithCovariance() {
		return amclPosition;
	}

	/**
	 * Probabilistic position of the robot computed by the <a href="http://wiki.ros.org/amcl">AMCL</a> algorithm.
	 * 
	 * @return The probabilistic position of the robot.
	 */
	public Position getPosition() {
		return new Position(amclPosition.position.x, amclPosition.position.y, amclPosition.position.z);
	}

	/**
	 * Move base result
	 * 
	 * @return Move base result
	 */
	public MoveResult getMoveBaseResult() {
		return moveBaseResult;
	}

	/**
	 * Set a goal for the "move base" service. After the goal is set the robot starts to move to reach it. The goal
	 * consist of a position and an orientation.
	 * 
	 * @param position
	 *            The desired position of the robot.
	 * @param orientation
	 *            The desired orientation of the robot.
	 */
	public void setSimpleGoal(ROSPosition position, Orientation orientation) {
		simpleGoal.getPosition().setX(position.x);
		simpleGoal.getPosition().setY(position.y);
		simpleGoal.getPosition().setZ(position.z);
		simpleGoal.getOrientation().setX(orientation.x);
		simpleGoal.getOrientation().setY(orientation.y);
		simpleGoal.getOrientation().setZ(orientation.z);
		simpleGoal.getOrientation().setW(orientation.w);

		synchronized (simpleGoalLock) {
			simpleGoalLock.notify();
		}
	}

	/**
	 * Transform given {@link geometry_msgs.Point} into {@link ROSPosition} instance.
	 * 
	 * @param point
	 *            The {@link geometry_msgs.Point} to be transformed.
	 * @return A {@link ROSPosition} with the values taken from the given point argument.
	 */
	private ROSPosition pointFromMessage(geometry_msgs.Point point) {
		return new ROSPosition(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Transform given {@link geometry_msgs.PoseWithCovariance} into {@link PoseWithCovariance} instance.
	 * 
	 * @param pose
	 *            The {@link geometry_msgs.PoseWithCovariance} to be transformed.
	 * @return A {@link PoseWithCovariance} with the values taken from the given point argument.
	 */
	private PoseWithCovariance poseFromMessage(geometry_msgs.PoseWithCovariance pose) {
		geometry_msgs.Point point = pose.getPose().getPosition();
		geometry_msgs.Quaternion orientation = pose.getPose().getOrientation();
		return new PoseWithCovariance(new ROSPosition(point.getX(), point.getY(), point.getZ()),
				new Orientation(orientation.getX(), orientation.getY(), orientation.getZ(), orientation.getW()),
				pose.getCovariance());
	}

}
