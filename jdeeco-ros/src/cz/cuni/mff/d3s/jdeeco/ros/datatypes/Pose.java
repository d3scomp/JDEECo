package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import java.io.Serializable;
import java.util.Locale;

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
	public final Position position;

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
	public Pose(Position position, Orientation orientation){
		this.position = position;
		this.orientation = orientation;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ENGLISH,
				"Position: %s Orientation: %s", position, orientation);
	}
	
}
