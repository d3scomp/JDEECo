package cz.cuni.mff.d3s.jdeeco.ros;

import kobuki_msgs.VersionInfo;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Subscriber;

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
	private static final String INFO_TOPIC = "/mobile_base/version_info";
	
	/**
	 * The topic for version info messages.
	 */
	private Subscriber<VersionInfo> infoTopic = null;

	/**
	 * The firmware info.
	 */
	private String firmwareInfo;
	/**
	 * The hardware info.
	 */
	private String hardwareInfo;
	/**
	 * The software info.
	 */
	private String softwareInfo;

	/**
	 * Internal constructor enables the {@link RosServices} to be in the control
	 * of instantiating {@link Info}.
	 */
	Info() {
		firmwareInfo = "";
		hardwareInfo = "";
		softwareInfo = "";
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
				INFO_TOPIC, VersionInfo._TYPE);
		infoTopic.addMessageListener(new MessageListener<VersionInfo>() {
			@Override
			public void onNewMessage(VersionInfo message) {
				firmwareInfo = message.getFirmware();
				hardwareInfo = message.getHardware();
				softwareInfo = message.getSoftware();
				// TODO: log
			}
		});
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
	 * Get the firmware info.
	 * 
	 * @return The firmware info.
	 */
	public String getFirmwareInfo() {
		return firmwareInfo;
	}

	/**
	 * Get the hardware info.
	 * 
	 * @return The hardware info.
	 */
	public String getHardwareInfo() {
		return hardwareInfo;
	}

	/**
	 * Get the software info.
	 * 
	 * @return The software info.
	 */
	public String getSoftwareInfo() {
		return softwareInfo;
	}
}
