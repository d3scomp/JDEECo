package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import java.io.Serializable;

/**
 * {@link Pose} contains Cartesian coordinates to determine a
 * position and orientation.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class Pose implements Serializable {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 8747842228380899999L;

	/**
	 * The position coordinates.
	 */
	public final Point position;

	/**
	 * The orientation coordinates.
	 */	
	public final Orientation orientation;

	/**
	 * Create a new pose from the given values.
	 * 
	 * @param position
	 *            The position coordinates.
	 * @param orientation
	 *            The orientation coordinates.
	 */
	public Pose(Point position, Orientation orientation){
		this.position = position;
		this.orientation = orientation;
	}
}
