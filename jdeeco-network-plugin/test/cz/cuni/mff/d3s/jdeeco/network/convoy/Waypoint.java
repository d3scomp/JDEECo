package cz.cuni.mff.d3s.jdeeco.network.convoy;

import java.io.Serializable;



/**
 * Represents a position in a 2D plane.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
@SuppressWarnings("serial")
public class Waypoint implements Serializable {
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
	public int hashCode() {
		return x & y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
