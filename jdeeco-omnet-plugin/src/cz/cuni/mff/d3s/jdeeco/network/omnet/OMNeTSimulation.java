package cz.cuni.mff.d3s.jdeeco.network.omnet;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.simulation.omnet.OMNeTNative;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;

public class OMNeTSimulation implements DEECoPlugin {
	class TimerProvider implements SimulationTimer {
		@Override
		public void notifyAt(long time, TimerEventListener listener) {
			
		}

		@Override
		public long getCurrentMilliseconds() {
			double time = OMNeTNative.nativeGetCurrentTime();
			return (long) (time * 1000);
		}

		@Override
		public void start(long duration) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private TimerProvider timeProvider = new TimerProvider();
	
	public SimulationTimer getTimer() {
		return timeProvider;
	}
	
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return new LinkedList<Class<? extends DEECoPlugin>>();
	}

	@Override
	public void init(DEECoContainer container) {
		throw new UnsupportedOperationException();
	}
}
