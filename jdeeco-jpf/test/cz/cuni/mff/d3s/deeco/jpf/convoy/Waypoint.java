package cz.cuni.mff.d3s.deeco.jpf.convoy;

/** Represents a position in a 2D plane.
 */
public class Waypoint {
	public Waypoint() {	
	}
	
	public Waypoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Integer x, y;
	
	@Override
	public boolean equals(Object that) {
		if (that instanceof Waypoint) {
			Waypoint thatWaypoint = (Waypoint)that;
			return thatWaypoint.x.equals(x) && thatWaypoint.y.equals(y);
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
