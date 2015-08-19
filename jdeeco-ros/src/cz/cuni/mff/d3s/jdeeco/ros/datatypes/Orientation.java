package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import java.io.Serializable;

/**
 * {@link Orientation} contains Cartesian coordinates X, Y, Z and W to determine
 * an orientation.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class Orientation implements Serializable {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 4691538165278800624L;

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
	 * The W coordinate.
	 */
	public final double w;

	/**
	 * Create a new instance of {@link Orientation} from the given values.
	 * 
	 * @param x
	 *            The X coordinate.
	 * @param y
	 *            The Y coordinate.
	 * @param z
	 *            The Z coordinate.
	 * @param w
	 *            The W coordinate.
	 */
	public Orientation(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

}
