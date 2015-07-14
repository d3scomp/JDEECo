package cz.cuni.mff.d3s.jdeeco.ros;

import java.util.HashMap;
import java.util.Map;

import kobuki_msgs.ButtonEvent;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.jdeeco.ros.datatypes.ButtonID;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.ButtonState;

/**
 * Provides methods to check robot's buttons state through ROS. Registration of
 * appropriate ROS topics is handled in the
 * {@link #subscribeDescendant(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Buttons extends TopicSubscriber {

	/**
	 * The name of the topic for button state updates.
	 */
	private static final String BUTTON_TOPIC = "/mobile_base/events/button";

	/**
	 * The state of turtlebot's buttons.
	 */
	private Map<ButtonID, ButtonState> buttonState;

	/**
	 * Internal constructor enables the {@link RosServices} to be in the control
	 * of instantiating {@link Buttons}.
	 */
	Buttons(){
		buttonState = new HashMap<>();
		for (ButtonID button : ButtonID.values()) {
			buttonState.put(button, ButtonState.RELEASED);
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
		Subscriber<ButtonEvent> buttonTopic = connectedNode.newSubscriber(
				BUTTON_TOPIC, ButtonEvent._TYPE);
		buttonTopic.addMessageListener(new MessageListener<ButtonEvent>() {
			@Override
			public void onNewMessage(ButtonEvent message) {
				ButtonID button = ButtonID.fromByte(message.getButton());
				ButtonState state = ButtonState.fromByte(message.getState());
				if (button != null && state != null) {
					buttonState.put(button, state);
				}
				// TODO: log
			}
		});
	}
	
	/**
	 * Get the state of the specified button.
	 * 
	 * @param button
	 *            The required button to get the state of.
	 * @return The state of the specified button. Null if the specified button
	 *         is not valid.
	 */
	public ButtonState getButtonState(ButtonID button) {
		if (buttonState.containsKey(button)) {
			return buttonState.get(button);
		}
		return null;
	}
}
