package cz.cuni.mff.d3s.deeco.simulation;

import static cz.cuni.mff.d3s.deeco.simulation.omnet.OMNetSimulation.timeDoubleToLong;
import cz.cuni.mff.d3s.deeco.network.Host;
import cz.cuni.mff.d3s.deeco.network.NetworkProvider;
import cz.cuni.mff.d3s.deeco.network.PositionProvider;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

public class SimulationHost extends Host {

	public SimulationHost(NetworkProvider networkProvider,
			PositionProvider positionProvider,
			CurrentTimeProvider timeProvider, String jDEECoAppModuleId) {
		super(networkProvider, positionProvider, timeProvider,
				jDEECoAppModuleId);
	}

	protected SimulationHost(NetworkProvider networkProvider,
			PositionProvider positionProvider,
			CurrentTimeProvider timeProvider, String jDEECoAppModuleId,
			boolean hasMANETNic, boolean hasIPNic) {
		super(networkProvider, positionProvider, timeProvider,
				jDEECoAppModuleId, hasMANETNic, hasIPNic);
	}

	private SimulationTimeEventListener timeEventListener = null;

	public void setSimulationTimeEventListener(
			SimulationTimeEventListener timeEventListener) {
		this.timeEventListener = timeEventListener;
	}

	public void at(double absoluteTime) {
		timeEventListener.at(timeDoubleToLong(absoluteTime));
	}
}
