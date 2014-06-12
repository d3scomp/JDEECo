package cz.cuni.mff.d3s.deeco.simulation;

import cz.cuni.mff.d3s.deeco.network.Host;
import cz.cuni.mff.d3s.deeco.network.NetworkProvider;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

public abstract class Simulation implements CurrentTimeProvider,
		CallbackProvider {

	protected NetworkProvider networkProvider;

	public Simulation(NetworkProvider networkProvider) {
		this.networkProvider = networkProvider;
	}
	
	/**
	 * Creates new instance of the {@link Host}.
	 * 
	 * @return new host instance
	 */
	public SimulationHost getHost(String logicalId, String networkId) {
		return getHost(logicalId, networkId, true, true);
	}

	public SimulationHost getHost(String logicalId, String networkId,
			boolean hasMANETNic, boolean hasEthernetNic) {
		SimulationHost host = (SimulationHost) networkProvider.getNetworkInterfaceByNetworkAddress(networkId);
		if (host == null) {
			host = new SimulationHost(networkProvider, this, logicalId,
					hasMANETNic, hasEthernetNic);
			networkProvider.registerInNetwork(host, networkId);
		}
		return host;
	}
	
	public NetworkProvider getNetworkProvider() {
		return networkProvider;
	}
}
