package cz.cuni.mff.d3s.deeco.simulation.omnet;

import static cz.cuni.mff.d3s.deeco.simulation.Simulation.secondsToMilliseconds;
import cz.cuni.mff.d3s.deeco.network.Host;
import cz.cuni.mff.d3s.deeco.network.NetworkProvider;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.simulation.SimulationTimeEventListener;
import cz.cuni.mff.d3s.deeco.simulation.SimulationTimeEventListenerHolder;

public class OMNetSimulationHost extends Host implements SimulationTimeEventListenerHolder {
	
	private SimulationTimeEventListener timeEventListener = null;

	public OMNetSimulationHost(NetworkProvider networkProvider,
			CurrentTimeProvider timeProvider, String jDEECoAppModuleId,
			boolean hasMANETNic, boolean hasIPNic) {
		super(networkProvider, timeProvider,
				jDEECoAppModuleId, hasMANETNic, hasIPNic);
	}

	public void setSimulationTimeEventListener(
			SimulationTimeEventListener timeEventListener) {
		this.timeEventListener = timeEventListener;
	}

	public void at(double absoluteTime) {
		timeEventListener.at(secondsToMilliseconds(absoluteTime));
	}
}
