package cz.cuni.mff.d3s.jdeeco.ros;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Weather;
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
	private static final String TEMPERATURE_TOPIC = "sht1x/temperature";
	/**
	 * The name of the topic for messages from humidity sensor.
	 */
	private static final String HUMIDITY_TOPIC = "sht1x/humidity";

	/**
	 * The topic for messages from temperature.
	 */
	private Subscriber<Temperature> temperatureTopic = null;
	/**
	 * The topic for messages from humidity sensor.
	 */
	private Subscriber<RelativeHumidity> humidityTopic = null;
	/**
	 * The last measured temperature.
	 */
	private double temperature;
	/**
	 * The last measured humidity.
	 */
	private double humidity;

	/**
	 * Create a new instance of {@link SHT1x}.
	 */
	public SHT1x() {
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
		temperatureTopic = connectedNode.newSubscriber(rosServices.getNamespace() + TEMPERATURE_TOPIC, Temperature._TYPE);
		temperatureTopic.addMessageListener(new MessageListener<Temperature>() {
			@Override
			public void onNewMessage(Temperature message) {
				temperature = message.getTemperature();

				Log.d(String.format("Temperature received: %f.", temperature));
			}
		});

		// Subscribe for humidity messages
		humidityTopic = connectedNode.newSubscriber(rosServices.getNamespace() + HUMIDITY_TOPIC, RelativeHumidity._TYPE);
		humidityTopic.addMessageListener(new MessageListener<RelativeHumidity>() {
			@Override
			public void onNewMessage(RelativeHumidity message) {
				humidity = message.getRelativeHumidity();

				Log.d(String.format("Humidity received: %f.", humidity));
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
		if (temperatureTopic != null) {
			temperatureTopic.shutdown();
		}
		if (humidityTopic != null) {
			humidityTopic.shutdown();
		}
	}

	/**
	 * Get the weather measured by SHT1x board. The weather consists of humidity
	 * and temperature.
	 * 
	 * @return The last measured weather values.
	 */
	public Weather getWeather() {
		return new Weather(humidity, temperature);
	}
}
