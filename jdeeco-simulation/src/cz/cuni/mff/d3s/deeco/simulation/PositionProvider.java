package cz.cuni.mff.d3s.deeco.simulation;

public interface PositionProvider {
	public boolean isPositionSensorAvailable(Host host);
	public double getPositionX(Host host);
	public double getPositionY(Host host);
	public double getPositionZ(Host host);
}
