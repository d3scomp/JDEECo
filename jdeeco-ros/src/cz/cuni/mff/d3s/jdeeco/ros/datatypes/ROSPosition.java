package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import java.io.Serializable;
import java.util.Locale;

import cz.cuni.mff.d3s.jdeeco.position.Position;

/**
 * {@link ROSPosition} contains Cartesian coordinates X, Y and Z to determine a position.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ROSPosition implements Serializable {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -8129512545084715014L;

	/**
	 * The X coordinate.
	 */
	public final double x;

	/**
	 * The Y coordinate.
	 */
	public final double y;

	/**
	 * The Z coordinate.
	 */
	public final double z;

	/**
	 * Create a new {@link ROSPosition} with the given coordinate values.
	 * 
	 * @param x
	 *            The X coordinate.
	 * @param y
	 *            The Y coordinate.
	 * @param z
	 *            The Z coordinate.
	 */
	public ROSPosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creates ROSPosition from jDEECo native position
	 * 
	 * @param position
	 *            Source position
	 * @return ROSPositioninstance with information from source position
	 */
	public static ROSPosition fromPosition(Position position) {
		return new ROSPosition(position.x, position.y, position.z);
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "[%f, %f, %f]", x, y, z);
	}
}
