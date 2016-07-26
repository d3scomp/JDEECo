package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import cz.cuni.mff.d3s.deeco.network.Host;
import cz.cuni.mff.d3s.deeco.simulation.omnet.OMNetSimulation;

public class PositionSensor {
	
	private final Host host;
	private final OMNetSimulation simulation;
	
	public PositionSensor(Host host, OMNetSimulation simulation) {
		this.host = host;
		this.simulation = simulation;
	}
	
	public double getX() {
		return simulation.getPositionX(host);
	}
	
	public double getY() {
		return simulation.getPositionY(host);
	}
}
