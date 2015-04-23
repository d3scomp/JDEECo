package cz.cuni.mff.d3s.jdeeco.network.omnet;

import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTSimulation.OMNeTHost;
import cz.cuni.mff.d3s.jdeeco.simulation.SimulationProvider;

public interface IOMNeTSimulation extends DEECoPlugin, SimulationProvider {
	public OMNeTHost getHost(int id);
}
