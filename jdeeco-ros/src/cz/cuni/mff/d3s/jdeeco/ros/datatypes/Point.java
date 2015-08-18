package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import java.io.Serializable;

/**
 * {@link Point} contains Cartesian coordinates X, Y and Z to determine a
 * position.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class Point implements Serializable {

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
	 * Create a new {@link Point} with the given coordinate values.
	 * 
	 * @param x
	 *            The X coordinate.
	 * @param y
	 *            The Y coordinate.
	 * @param z
	 *            The Z coordinate.
	 */
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
