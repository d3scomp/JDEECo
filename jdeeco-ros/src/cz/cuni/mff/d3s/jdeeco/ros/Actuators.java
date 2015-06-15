package cz.cuni.mff.d3s.jdeeco.ros;

import geometry_msgs.Twist;

import org.ros.concurrent.CancellableLoop;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

public class Actuators extends TopicSubscriber {

	private static final String VELOCITY_TOPIC = "/mobile_base/commands/velocity";

	private static final double MAX_LINEAR_VELOCITY = 1.5; // Value taken from kobuki_keyop
	private static final double MAX_ANGULAR_VELOCITY = 6.6; // Value taken from kobuki_keyop

	private double linearVelocity;
	private double angularVelocity;

	private static Actuators INSTANCE;

	private Actuators() {
	}

	public static Actuators getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Actuators();
		}
		return INSTANCE;
	}

	@Override
	void subscribe(ConnectedNode connectedNode) {
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
				twist.getLinear().setX(linearVelocity);
				twist.getLinear().setY(0);
				twist.getLinear().setZ(0);
				twist.getAngular().setX(0);
				twist.getAngular().setY(0);
				twist.getAngular().setZ(angularVelocity);
				System.out.println(String.format("Velocity: [%f, %f]", twist.getLinear().getX(), twist.getAngular().getZ()));
				velocityTopic.publish(twist);
				Thread.sleep(100); // If the values are not send within 600 ms turtlebot stops
			}
		});
	}

	public void setVelocity(double linear, double angular) {
		linearVelocity = fromNormalized(linear, MAX_LINEAR_VELOCITY);
		angularVelocity = fromNormalized(angular, MAX_ANGULAR_VELOCITY);
	}

	private double fromNormalized(double normalized, double max) {
		return normalized * max;
	}

}
