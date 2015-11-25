package cz.cuni.mff.d3s.jdeeco.ros;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.InfoData;
import kobuki_msgs.VersionInfo;

/**
 * Provides methods to obtain information about the robot through ROS.
 * Registration of appropriate ROS topics is handled in the
 * {@link #subscribeDescendant(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Info extends TopicSubscriber {

	/**
	 * The name of the topic for version info messages.
	 */
	private static final String INFO_TOPIC = "mobile_base/version_info";
	
	/**
	 * The topic for version info messages.
	 */
	private Subscriber<VersionInfo> infoTopic = null;

	/**
	 * The robot's version information.
	 */
	private InfoData infoData;
	
	/**
	 * Create a new instance of  {@link Info}.
	 */
	public Info() {
		infoData = new InfoData("", "", "");
	}

	/**
	 * Register and subscribe to required ROS topics.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	protected void subscribeDescendant(ConnectedNode connectedNode) {
		infoTopic = connectedNode.newSubscriber(
				rosServices.getNamespace() + INFO_TOPIC, VersionInfo._TYPE);
		infoTopic.addMessageListener(new MessageListener<VersionInfo>() {
			@Override
			public void onNewMessage(VersionInfo message) {
				infoData = new InfoData(
						message.getFirmware(),
						message.getHardware(),
						message.getSoftware());

				Log.d("Mobile base version info received.");
			}
		}, MESSAGE_QUEUE_LIMIT);
	}

	/**
	 * Finalize the connection to ROS topics.
	 * 
	 * @param node
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	void unsubscribe(Node node) {
		if(infoTopic != null){
			infoTopic.shutdown();
		}
	}

	/**
	 * Get robot's version information.
	 * 
	 * @return The robot's version information.
	 */
	public InfoData getInfo(){
		return infoData;
	}
	
}
