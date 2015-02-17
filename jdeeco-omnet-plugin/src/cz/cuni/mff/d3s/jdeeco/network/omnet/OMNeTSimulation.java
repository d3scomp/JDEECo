package cz.cuni.mff.d3s.jdeeco.network.omnet;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;

public class OMNeTSimulation implements SimulationTimer, DEECoPlugin {
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return new LinkedList<Class<? extends DEECoPlugin>>();
	}

	@Override
	public void init(DEECoContainer container) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getCurrentMilliseconds() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notifyAt(long time, TimerEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(long duration) {
		// TODO Auto-generated method stub
		
	}
}
