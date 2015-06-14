package cz.cuni.mff.d3s.jdeeco.ros;

import org.ros.node.ConnectedNode;

public abstract class TopicSubscriber {
	abstract void subscribe(ConnectedNode connectedNode);
}
