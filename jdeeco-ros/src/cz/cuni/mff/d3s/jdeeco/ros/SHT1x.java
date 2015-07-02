package cz.cuni.mff.d3s.jdeeco.ros;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import sensor_msgs.RelativeHumidity;
import sensor_msgs.Temperature;

/**
 * Provides methods to check the temperature and humidity read by the SHT1x
 * sensor and distributed through ROS. Registration of appropriate ROS topics is
 * handled in the {@link #subscribeDescendant(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class SHT1x extends TopicSubscriber {

	/**
	 * The name of the topic for messages from temperature.
	 */
	private static final String TEMPERATURE_TOPIC = "/sht1x/temperature";
	/**
	 * The name of the topic for messages from humidity sensor.
	 */
	private static final String HUMIDITY_TOPIC = "/sht1x/humidity";

	/**
	 * The last measured temperature.
	 */
	private double temperature;
	/**
	 * The last measured humidity.
	 */
	private double humidity;

	/**
	 * Internal constructor enables the {@link RosServices} to be in the control
	 * of instantiating {@link SHT1x}.
	 */
	SHT1x() {
		temperature = 0;
		humidity = 0;
	}

	/**
	 * Register and subscribe to required ROS topics of sensor readings.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	protected void subscribeDescendant(ConnectedNode connectedNode) {
		// Subscribe for temperature messages
		Subscriber<Temperature> temperatureTopic = connectedNode.newSubscriber(
				TEMPERATURE_TOPIC, Temperature._TYPE);
		temperatureTopic.addMessageListener(new MessageListener<Temperature>() {
			@Override
			public void onNewMessage(Temperature message) {
				temperature = message.getTemperature();
				// TODO: log
			}
		});

		// Subscribe for humidity messages
		Subscriber<RelativeHumidity> humidityTopic = connectedNode
				.newSubscriber(HUMIDITY_TOPIC, RelativeHumidity._TYPE);
		humidityTopic
				.addMessageListener(new MessageListener<RelativeHumidity>() {
					@Override
					public void onNewMessage(RelativeHumidity message) {
						humidity = message.getRelativeHumidity();
						// TODO: log
					}
				});
	}

	/**
	 * Get the temperature
	 * 
	 * @return The last measured temperature.
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Get the humidity.
	 * 
	 * @return The last measured humidity.
	 */
	public double getHumidity() {
		return humidity;
	}
}
