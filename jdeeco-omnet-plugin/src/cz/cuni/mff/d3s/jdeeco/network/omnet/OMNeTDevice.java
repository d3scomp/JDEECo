package cz.cuni.mff.d3s.jdeeco.network.omnet;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.Device;

/**
 * Generic OMNeT network device
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public abstract class OMNeTDevice extends Device implements DEECoPlugin {
	protected OMNeTSimulation omnet;
	protected Network network;

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		Arrays.asList(Network.class, OMNeTSimulation.class);
		return null;
	}
	
	@Override
	public void init(DEECoContainer container) {
		// Resolve dependencies
		network = container.getPluginInstance(Network.class);
		omnet = container.getPluginInstance(OMNeTSimulation.class);
	}
}
