package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import java.io.Serializable;
import java.util.Locale;

/**
 * Contains values provided by the SHT1x board. These values include humidity
 * and temperature.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class Weather implements Serializable {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -8701496085469508263L;

	/**
	 * Humidity expressed in percentage.
	 */
	public final double humidity;

	/**
	 * Temperature expressed in centigrade (degrees Celsius).
	 */
	public final double temperature;

	/**
	 * Create new instance of weather values provided by the SHT1x board.
	 * @param humidity The humidity in percentage.
	 * @param temperature The temperature in centigrade (degrees Celsius).
	 */
	public Weather(double humidity, double temperature) {
		this.humidity = humidity;
		this.temperature = temperature;
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, 
				"Temperature: %f Humidity: %f",
				temperature, humidity);
	}
}
