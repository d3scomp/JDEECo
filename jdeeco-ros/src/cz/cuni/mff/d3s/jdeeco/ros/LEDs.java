package cz.cuni.mff.d3s.jdeeco.ros;

import java.util.HashMap;
import java.util.Map;

import kobuki_msgs.Led;

import org.ros.concurrent.CancellableLoop;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedColor;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedID;

public class LEDs extends TopicSubscriber {
	/**
	 * A {@link Map} of available LEDs and theirs assigned colors.
	 */
	private Map<LedID, LedColor> ledColor;

	/**
	 * Internal constructor enables the {@link RosServices} to be in the control
	 * of instantiating {@link LEDs}.
	 */
	LEDs(){
		ledColor = new HashMap<>();
	}

	/**
	 * Subscribe all the LEDs to ROS topics for them. To publish color changes
	 * wait until notified by the {@link #setLed(LedID, LedColor)} setter.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	protected void subscribeDescendant(ConnectedNode connectedNode) {
		for (LedID ledId : LedID.values()) {
			final Publisher<Led> ledTopic = connectedNode.newPublisher(
					ledId.topic, Led._TYPE);
			connectedNode.executeCancellableLoop(new CancellableLoop() {
				@Override
				protected void setup() {
					ledColor.put(ledId, LedColor.BLACK);
				}

				@Override
				protected void loop() throws InterruptedException {
					Led led = ledTopic.newMessage();
					led.setValue(ledColor.get(ledId).value);
					ledTopic.publish(led);
					// TODO: log

					synchronized (ledId) {
						ledId.wait();
					}
				}
			});
		}
	}

	/**
	 * Set desired color to a LED of your choice. {@link LedColor#BLACK} turns
	 * the LED off. Setting the color notifies the appropriate ROS publisher to
	 * publish the message in the topic for the LED.
	 * 
	 * @param ledId
	 *            The LED to control.
	 * @param color
	 *            The desired color of the LED.
	 */
	public void setLed(LedID ledId, LedColor color) {
		ledColor.put(ledId, color);
		synchronized (ledId) {
			ledId.notify();
		}
	}
}
