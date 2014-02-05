package cz.cuni.mff.d3s.jdeeco.simulation.demo;

/**
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class CircularArea extends Area {

	private final double x, y, r;
	
	public CircularArea(String id, double x, double y, double r, String[] teams) {
		super(id, teams);
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	@Override
	public boolean isInArea(Position pos) {
		return Math.pow(pos.x - x, 2) + Math.pow(pos.y - y, 2) < Math.pow(r+TOLERANCE, 2);
	}

}
