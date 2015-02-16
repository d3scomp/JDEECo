package cz.cuni.mff.d3s.jdeeco.network.omnet;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;

public class OMNeTSimulation implements Notifier, DEECoPlugin {
	@Override
	public void notifyAt(long time, DummyListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getCurrentTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return new LinkedList<Class<? extends DEECoPlugin>>();
	}

	@Override
	public void init(DEECoContainer container) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void start(long duration) {
		throw new UnsupportedOperationException();
	}
}
