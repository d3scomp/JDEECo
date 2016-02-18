package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Position implements Serializable {

	public double y;
	public double x;

	public Position() {
	}

	public Position(double x, double y) {
		this.y = y;
		this.x = x;
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
		return (y == other.y) && (x == other.x);
	}

	@Override
	public int hashCode() {
		return (int) (y * 1024 + x);
	}

	@Override
	public String toString() {
		return String.format("[%d,%d]", y, x);
	}
}
