package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import java.io.Serializable;
import java.util.Locale;

/**
 * Represents a data from a GPS module.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class GpsData implements Serializable {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -8229644198007362719L;

	/**
	 * The latitude.
	 */
	public final double latitude;

	/**
	 * The longitude.
	 */
	public final double longitude;

	/**
	 * The altitude.
	 */
	public final double altitude;

	/**
	 * The GPS time in milliseconds.
	 */
	public final long time;

	/**
	 * Create a new instance of GPS data with the given values.
	 * 
	 * @param latitude
	 *            The latitude.
	 * @param longitude
	 *            The longitude.
	 * @param altitude
	 *            The altitude.
	 * @param time
	 *            The time in milliseconds.
	 */
	public GpsData(double latitude, double longitude, double altitude, long time) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.time = time;
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH,
				"Lat: %f Lon: %f Alt: %f Time: %d",
				latitude, longitude, altitude, time);
	}
}
