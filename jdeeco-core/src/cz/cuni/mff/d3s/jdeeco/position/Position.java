package cz.cuni.mff.d3s.jdeeco.position;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents coordinates in Cartesian coordinate system.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class Position implements Serializable, Cloneable {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -5980471557237426848L;

	/** X-coordinate. */
	public final double x;
	/** Y-coordinate. */
	public final double y;
	/** Z-coordinate. */
	public final double z;

	/**
	 * Construct a {@link Position} within three dimensions.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 */
	public Position(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Construct a {@link Position} in the Z plane.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	
	/**
	 * Compute the Euclidian distance between this {@link Position} and the given position.
	 * @param position The {@link Position} to calculate the distance to.
	 * @return The Euclidian distance between this {@link Position} and the given position.
	 */
	public double euclidDistanceTo(Position position) {
		double dx = Math.pow(x - position.x, 2);
		double dy = Math.pow(y - position.y, 2);
		double dz = Math.pow(z - position.z, 2);
		return Math.sqrt(dx + dy + dz);
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "[%.1fm, %.1fm, %.1fm]", x, y, z);
	}

	@Override
	public Position clone() {
		try {
			return (Position) super.clone();
		} catch (CloneNotSupportedException e) {
			return null; //should never happen
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Position) {
			Position other = (Position)obj;
			return other.x == x && other.y == y && other.z == z;
		} else {
			return super.equals(obj);
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
}
