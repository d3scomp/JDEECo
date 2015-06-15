package cz.cuni.mff.d3s.jdeeco.ros;

import org.ros.node.ConnectedNode;

/**
 * An ancestor of classes which register of subscribe to any ROS topic. The
 * {@link #subscribe(ConnectedNode)} method has to be overridden in a derived
 * class and serves for the registration and subscription of topics. The
 * {@link TopicSubscriber} is a abstract class rather than interface because the
 * {@link #subscribe(ConnectedNode)} method is internal to the
 * {@link cz.cuni.mff.d3s.jdeeco.ros} package.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public abstract class TopicSubscriber {
	/**
	 * This method is invoked during the initialization of the
	 * {@link RosServices} DEECo plugin. In this method the derived class is
	 * supposed to register and subscribe to required ROS topics.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	abstract void subscribe(ConnectedNode connectedNode);
}
