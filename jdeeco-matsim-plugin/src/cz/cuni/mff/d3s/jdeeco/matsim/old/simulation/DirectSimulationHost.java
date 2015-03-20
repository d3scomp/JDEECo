package cz.cuni.mff.d3s.jdeeco.matsim.old.simulation;

import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

public class DirectSimulationHost extends AbstractHost implements SimulationTimeEventListenerHolder {
	private SimulationTimeEventListener timeEventListener = null;

	public DirectSimulationHost(String id, CurrentTimeProvider timeProvider) {
		super(id, timeProvider);
		// TODO Auto-generated constructor stub
	}

	public void setSimulationTimeEventListener(SimulationTimeEventListener timeEventListener) {
		this.timeEventListener = timeEventListener;
	}

	public void at(double absoluteTime) {
		timeEventListener.at(Simulation.secondsToMilliseconds(absoluteTime));
	}

	public String toString() {
		return "Host: " + id;
	}
}
