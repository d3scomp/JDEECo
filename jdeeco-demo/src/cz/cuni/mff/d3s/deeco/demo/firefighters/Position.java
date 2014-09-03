package cz.cuni.mff.d3s.deeco.demo.firefighters;

@SuppressWarnings("serial")
public class Position {

	public int latitude;
	public int longitude;

	public Position() {
	}

	public Position(int latitude, int longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Position))
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
