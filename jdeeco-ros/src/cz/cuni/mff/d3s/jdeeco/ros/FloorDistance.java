package cz.cuni.mff.d3s.jdeeco.ros;

import java.util.HashMap;
import java.util.Map;

import kobuki_msgs.CliffEvent;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.FloorSensorID;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.FloorSensorState;

/**
 * Provides methods to check the floor distance registered by robot's cliff
 * sensors through ROS. Registration of appropriate ROS topics is handled in the
 * {@link #subscribeDescendant(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class FloorDistance extends TopicSubscriber {

	/**
	 * The name of the topic for messages from the distance sensors pointed to
	 * the floor.
	 */
	private static final String CLIFF_TOPIC = "/mobile_base/events/cliff";

	/**
	 * The topic for messages from the distance sensors pointed to the floor.
	 */
	private Subscriber<CliffEvent> cliffTopic = null;

	/**
	 * The state sensed by the floor sensors.
	 */
	private Map<FloorSensorID, FloorSensorState> floorSensorStates;

	/**
	 * The distance to the floor.
	 */
	private Map<FloorSensorID, Short> floorDistances;

	/**
	 * Internal constructor enables the {@link RosServices} to be in the control
	 * of instantiating {@link FloorDistance}.
	 */
	FloorDistance() {
		floorSensorStates = new HashMap<>();
		floorDistances = new HashMap<>();
		for (FloorSensorID sensor : FloorSensorID.values()) {
			floorSensorStates.put(sensor, FloorSensorState.FLOOR);
			floorDistances.put(sensor, (short) 0);
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
		cliffTopic = connectedNode.newSubscriber(
				CLIFF_TOPIC, CliffEvent._TYPE);
		cliffTopic.addMessageListener(new MessageListener<CliffEvent>() {
			@Override
			public void onNewMessage(CliffEvent message) {
				FloorSensorID sensor = FloorSensorID.fromByte(message
						.getSensor());
				FloorSensorState state = FloorSensorState.fromByte(message
						.getState());
				if (sensor != null) {
					if (state != null) {
						floorSensorStates.put(sensor, state);
					}
					floorDistances.put(sensor, message.getBottom());
				}
				Log.d(String.format("FloorDistance from sensor %d changed to %d.",
						message.getSensor(), message.getState()));
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
		if(cliffTopic != null){
			cliffTopic.shutdown();
		}
	}
	
	/**
	 * Get the state sensed by the specified floor sensor.
	 * 
	 * @param sensor
	 *            The sensor to get the state from.
	 * @return The state from the specified sensor. Null if the specified sensor
	 *         is not valid.
	 */
	public FloorSensorState getFloorSensorState(FloorSensorID sensor) {
		if (floorSensorStates.containsKey(sensor)) {
			return floorSensorStates.get(sensor);
		}
		return null;
	}

	/**
	 * Get the floor distance read by the specified sensor.
	 * 
	 * @param sensor
	 *            The sensor to get the distance from.
	 * @return The distance read by the specified sensor. Null if the specified
	 *         sensor is not valid.
	 */
	public Short getFloorDistance(FloorSensorID sensor) {
		// TODO: find out from the kobuki_msgs.CliffEvent source code what the
		// getBottom number really means
		if (floorDistances.containsKey(sensor)) {
			return floorDistances.get(sensor);
		}
		return null;
	}
}
