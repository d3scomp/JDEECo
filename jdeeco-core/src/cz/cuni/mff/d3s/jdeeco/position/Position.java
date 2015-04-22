package cz.cuni.mff.d3s.jdeeco.position;

/**
 * Holds node position information
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public final class Position {
	public final double x;
	public final double y;
	public final double z;
	
	public Position(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double euclidDistanceTo(Position position) {
		double dx = Math.abs(x - position.x);
		double dy = Math.abs(y - position.y);
		double dz = Math.abs(z - position.z);
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	@Override
	public String toString() {
		return String.format("[%.1fm, %.1fm, %.1fm]", x, y, z);
	}
}
