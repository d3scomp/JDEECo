package cz.cuni.mff.d3s.deeco.simulation;

import static cz.cuni.mff.d3s.deeco.simulation.Simulation.secondsToMilliseconds;
import cz.cuni.mff.d3s.deeco.network.Host;
import cz.cuni.mff.d3s.deeco.network.NetworkProvider;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

public class SimulationHost extends Host {

	public SimulationHost(NetworkProvider networkProvider,
			CurrentTimeProvider timeProvider, String jDEECoAppModuleId) {
		super(networkProvider, timeProvider,
				jDEECoAppModuleId);
	}

	public SimulationHost(NetworkProvider networkProvider,
			CurrentTimeProvider timeProvider, String jDEECoAppModuleId,
			boolean hasMANETNic, boolean hasIPNic) {
		super(networkProvider, timeProvider,
				jDEECoAppModuleId, hasMANETNic, hasIPNic);
	}

	private SimulationTimeEventListener timeEventListener = null;

	public void setSimulationTimeEventListener(
			SimulationTimeEventListener timeEventListener) {
		this.timeEventListener = timeEventListener;
	}

	public void at(double absoluteTime) {
		timeEventListener.at(secondsToMilliseconds(absoluteTime));
	}
}
