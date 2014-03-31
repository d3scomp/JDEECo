package cz.cuni.mff.d3s.jdeeco.simulation.demo;

/**
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class RectangularArea extends Area {

	private final double x, y, width, height;
	
	public RectangularArea(String id, double x, double y, double width, double height, String[] teams) {
		super(id, teams);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean isInArea(Position pos) {
		return pos.x >= (x-TOLERANCE) && pos.x <= (x+width+TOLERANCE) && pos.y >= (y-TOLERANCE) && pos.y <= (y+height+TOLERANCE);
	}

	@Override
	public double getCenterX() {
		return x + (width / 2.0);
	}

	@Override
	public double getCenterY() {
		// TODO Auto-generated method stub
		return y + (height / 2.0);
	}

}
