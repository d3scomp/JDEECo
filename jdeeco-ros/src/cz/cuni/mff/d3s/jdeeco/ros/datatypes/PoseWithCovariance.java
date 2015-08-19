package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

/**
 * {@link PoseWithCovariance} contains Cartesian coordinates to determine a
 * position and orientation provides the certainty covariance of the pose.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class PoseWithCovariance extends Pose {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 8369465610397092592L;

	/**
	 * The confidence covariance of the pose.
	 */
	public final double[] covariance;

	/**
	 * Create a new pose with associated covariance from the given values.
	 * 
	 * @param position
	 *            The position coordinates.
	 * @param orientation
	 *            The orientation coordinates.
	 * @param cov
	 *            The covariance.
	 */
	public PoseWithCovariance(Position position, Orientation orientation, double[] cov) {
		super(position, orientation);
		this.covariance = cov;
	}

}
