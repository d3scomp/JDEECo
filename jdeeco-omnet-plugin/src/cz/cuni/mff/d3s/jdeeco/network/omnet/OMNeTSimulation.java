package cz.cuni.mff.d3s.jdeeco.network.omnet;

import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;

public class OMNeTSimulation implements Notifier, DEECoPlugin {

	public OMNeTSimulation() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void notifyAt(long time, DummyListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getCurrentTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(DEECoContainer container) {
		// TODO Auto-generated method stub
		
		container.getPluginInstance(Network.class).getL1().registerDevice(device);

	}

}
