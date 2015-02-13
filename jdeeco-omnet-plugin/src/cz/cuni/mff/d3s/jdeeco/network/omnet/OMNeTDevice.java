package cz.cuni.mff.d3s.jdeeco.network.omnet;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.Device;

/**
 * Generic OMNeT network device
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public abstract class OMNeTDevice implements Device, DEECoPlugin {
	protected final OMNeTSimulation omnet;

	public OMNeTDevice(OMNeTSimulation simulation) {
		omnet = simulation;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		Arrays.asList(Network.class);
		return null;
	}
}
