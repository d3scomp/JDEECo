package cz.cuni.mff.d3s.jdeeco.ros;

import java.util.HashMap;
import java.util.Map;

import kobuki_msgs.DockInfraRed;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.DockingIRDiod;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.DockingIRSignal;

/**
 * Provides methods to check the signal from robot's IR docking diods
 * through ROS. Registration of appropriate ROS topics is handled
 * in the {@link #subscribeDescendant(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class DockIR extends TopicSubscriber {

	/**
	 * The name of the topic for messages from docking diods.
	 */
	private static final String DOCK_IR_TOPIC = "/mobile_base/sensors/dock_ir";

	/**
	 * The topic for messages from docking diods.
	 */
	private Subscriber<DockInfraRed> dockIrTopic = null;

	/**
	 * The signal from docking diods.
	 */
	private Map<DockingIRDiod, DockingIRSignal> dockingIRSignal;

	/**
	 * Internal constructor enables the {@link RosServices} to be in the control
	 * of instantiating {@link DockIR}.
	 */
	DockIR() {
		dockingIRSignal = new HashMap<>();
		for (DockingIRDiod diod : DockingIRDiod.values()) {
			dockingIRSignal.put(diod, DockingIRSignal.INFINITY);
		}
	}

	/**
	 * Register and subscribe to required ROS topics of sensor readings.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	protected void subscribeDescendant(ConnectedNode connectedNode) {
		dockIrTopic = connectedNode.newSubscriber(
				DOCK_IR_TOPIC, DockInfraRed._TYPE);
		dockIrTopic.addMessageListener(new MessageListener<DockInfraRed>() {
			@Override
			public void onNewMessage(DockInfraRed message) {
				ChannelBuffer b = message.getData();
				for (int i = 0; i < 3; i++) {
					dockingIRSignal.put(DockingIRDiod.fromIndex(i),
							DockingIRSignal.fromByte(b.getByte(i)));
				}
				Log.d(String.format("DockIR changed state to [%d, %d, %d].",
						b.getByte(0), b.getByte(1), b.getByte(2)));
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
		if(dockIrTopic != null){
			dockIrTopic.shutdown();
		}
	}
	
	/**
	 * Get the signal from the specified infra-red docking diod.
	 * 
	 * @param diod
	 *            The diod to get the signal from.
	 * @return The signal from the specified infra-red docking diod. Null if the
	 *         specified diod is not valid.
	 */
	public DockingIRSignal getDockingIRSignal(DockingIRDiod diod) {
		if (dockingIRSignal.containsKey(diod)) {
			return dockingIRSignal.get(diod);
		}
		return null;
	}

}
