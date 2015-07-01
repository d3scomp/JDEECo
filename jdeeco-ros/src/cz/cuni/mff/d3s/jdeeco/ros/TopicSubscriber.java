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
	 * Indicates whether the {@link #subscribe(ConnectedNode)} method has been
	 * called (true) or not (false).
	 */
	private boolean subscribed;

	/**
	 * Initialize new @{link TopicSubscriber} object.
	 */
	public TopicSubscriber() {
		subscribed = false;
	}

	/**
	 * This method is invoked during the initialization of the
	 * {@link RosServices} DEECo plugin. In this method the derived class is
	 * supposed to register and subscribe to required ROS topics. After this
	 * method was called the {@link #isSubscribed()} method returns true.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	final void subscribe(ConnectedNode connectedNode) {
		subscribeDescendant(connectedNode);
		subscribed = true;
	}

	/**
	 * Indicates whether the {@link #subscribe(ConnectedNode)} method has been
	 * called (true) or not (false).
	 * 
	 * @return True if the {@link #subscribe(ConnectedNode)} method has been
	 *         already called. False otherwise.
	 */
	protected boolean isSubscribed() {
		return subscribed;
	}

	/**
	 * In this method all the descendants of the {@link TopicSubscriber} has to
	 * subscribe to desired ROS topics.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	abstract protected void subscribeDescendant(ConnectedNode connectedNode);
}
