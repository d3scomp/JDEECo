package cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata;

import java.io.Serializable;

/**
 * Holds a boundary value together with a flag indicating whether the value
 * has changed since it was lastly used.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class BoundaryValueHolder implements Serializable {
	
	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -6438264656364851855L;

	/**
	 * The limit beyond which two doubles are considered equal.
	 * The value of this constant influences the granularity of changing
	 * the boundary.
	 */
	protected static final double EPSILON = 0.01;
	
	/**
	 * Stored value of the boundary.
	 */
	private double boundary;
	/**
	 * Flag indicating whether the boundary has changed enough (see {@link #EPSILON})
	 * since it was lastly used.
	 */
	private boolean changed;
	
	/**
	 * Construct new instance of the {@link BoundaryValueHolder} with the
	 * given value to hold. The {@link #hasChanged()} flag is set by default
	 * to true.
	 * @param boundary The boundary value to be held.
	 */
	public BoundaryValueHolder(double boundary) {
		this.boundary = boundary;
		this.changed = true;
	}
	
	/**
	 * Set new boundary to be held. If the boundary differs from the previous
	 * one more than {@link #EPSILON} than the {@link #hasChanged()} flag
	 * is set to true.
	 * @param boundary The boundary to be held.
	 */
	public void setBoundary(double boundary) {
		if(Double.isNaN(boundary) && Double.isNaN(this.boundary)) {
			return;
		}
		if(Double.isNaN(boundary) || Double.isNaN(this.boundary)
				|| Math.abs(this.boundary - boundary) > EPSILON) {
			this.boundary = boundary;
			changed = true;
		}
	}
	
	/**
	 * Read the stored boundary.
	 * @return The stored boundary.
	 */
	public double getBoundary() {
		return boundary;
	}
	
	/**
	 * Indicates whether the boundary is valid (is not NaN).
	 * @return True if the boundary is not NaN.
	 */
	public boolean isValid() {
		return !Double.isNaN(boundary);
	}
	
	/**
	 * Set the {@link #hasChanged()} flag to false. Indicate that
	 * the current value has been used.
	 */
	public void boundaryUsed() {
		changed = false;
	}
	
	/**
	 * Indicates whether the stored boundary value has changed enough
	 * (see {@link #EPSILON}) since its last usage.
	 * @return
	 */
	public boolean hasChanged() {
		return changed;
	}

}
