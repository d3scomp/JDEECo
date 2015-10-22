package cz.cuni.mff.d3s.jdeeco.ros;

import kobuki_msgs.BumperEvent;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.BumperValue;

/**
 * Provides methods to check robot's bumper state through ROS. Registration of
 * appropriate ROS topics is handled in the
 * {@link #subscribeDescendant(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Bumper extends TopicSubscriber {

	/**
	 * The name of the bumper topic.
	 */
	private static final String BUMPER_TOPIC = "mobile_base/events/bumper";

	/**
	 * The bumper topic.
	 */
	private Subscriber<BumperEvent> bumperTopic = null;
	
	/**
	 * The bumper state.
	 */
	private BumperValue bumper;

	/**
	 * Create a new instance of {@link Bumper}.
	 */
	public Bumper() {
		bumper = BumperValue.RELEASED;
	}

	/**
	 * Register and subscribe to required ROS topics of sensor readings.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	protected void subscribeDescendant(ConnectedNode connectedNode) {
		bumperTopic = connectedNode.newSubscriber(
				rosServices.getNamespace() + BUMPER_TOPIC, BumperEvent._TYPE);
		bumperTopic.addMessageListener(new MessageListener<BumperEvent>() {
			@Override
			public void onNewMessage(BumperEvent message) {
				byte state = message.getState();
				if (state == BumperEvent.RELEASED) {
					bumper = BumperValue.RELEASED;
				} else {
					bumper = BumperValue.fromByte(message.getBumper());
				}
				Log.d(String.format("Bumper state changed to %d.", message.getState()));
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
		if(bumperTopic != null){
			bumperTopic.shutdown();
		}
	}

	/**
	 * The bumper state published in the bumper topic.
	 * 
	 * @return the state of the bumper.
	 */
	public BumperValue getBumper() {
		return bumper;
	}

}
