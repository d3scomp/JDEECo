package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.io.Serializable;

public class Position implements Serializable {

	private static final long serialVersionUID = -1251527172175834548L;
		
	public int latitude;
	public int longitude;	
	
	public Position(int latitude, int longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if ( this == obj ) 
	    	return true;
	    if (obj == null)
	    	return false;
		if ( !(obj instanceof Position) ) 
			return false;
		Position other = (Position) obj;
		return (latitude == other.latitude) && (longitude == other.longitude);
	}
	
	@Override
	public int hashCode() {
		return latitude * 1024 + longitude;
	}
	
	@Override
	public String toString() {
		return String.format("[%d,%d]", latitude, longitude);
	}
}
