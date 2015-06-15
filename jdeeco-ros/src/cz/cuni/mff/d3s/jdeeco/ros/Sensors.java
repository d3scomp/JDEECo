package cz.cuni.mff.d3s.jdeeco.ros;

import geometry_msgs.Point;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

public class Sensors extends TopicSubscriber {

	private static final String ODOMETRY_TOPIC = "odom";

	private Point lastPosition;

	private static Sensors INSTANCE;

	private Sensors() {}

	public static Sensors getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Sensors();
		}
		return INSTANCE;
	}

	@Override
	void subscribe(ConnectedNode connectedNode) {
		Subscriber<nav_msgs.Odometry> odometryTopic = connectedNode
				.newSubscriber(ODOMETRY_TOPIC, nav_msgs.Odometry._TYPE);
		odometryTopic
				.addMessageListener(new MessageListener<nav_msgs.Odometry>() {
					@Override
					public void onNewMessage(nav_msgs.Odometry message) {

						lastPosition = message.getPose().getPose()
								.getPosition();
						/*System.out.format("Position: %f,%f%n",
								message.getPose().getPose().getPosition().getX(),
								message.getPose().getPose().getPosition().getY());*/
					
						// TODO: make own implementation of Point to lose
						// dependency on org.ros artifact?
					}
				});
	}

	public Point getPosition() {
		return lastPosition;
	}

}
