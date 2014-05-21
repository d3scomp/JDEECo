package cz.cuni.mff.d3s.deeco.simulation;

public interface SteppableSimulation extends SimulationTimeEventListener {
	public long getSimulationStep();
}
