package cz.cuni.mff.d3s.jdeeco.network.omnet;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.Device;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTSimulation.OMNeTHost;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;

/**
 * Generic OMNeT network device
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public abstract class OMNeTDevice extends Device implements DEECoPlugin {
	protected OMNeTSimulation omnet;
	protected OMNeTHost host;
	protected Network network;
	protected int id;
	
	static int counter = 0;
	public OMNeTDevice() {
		id = counter++;
	}
	
	// TODO: Return device address
	@Override
	public String getId() {
		return String.valueOf(id);
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class, OMNeTSimulation.class, PositionPlugin.class);
	}
	
	@Override
	public void init(DEECoContainer container) {
		// Resolve dependencies
		network = container.getPluginInstance(Network.class);
		omnet = container.getPluginInstance(OMNeTSimulation.class);
		host = omnet.getHost(container.getId());
		
		network.getL1().registerDevice(this);
	}
}
