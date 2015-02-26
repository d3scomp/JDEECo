package cz.cuni.mff.d3s.jdeeco.network.omnet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.simulation.omnet.OMNeTNative;
import cz.cuni.mff.d3s.deeco.simulation.omnet.OMNeTNativeListener;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;

public class OMNeTSimulation implements DEECoPlugin {
	Map<Integer, TimerEventListener> binding = new HashMap<Integer, TimerEventListener>();

	class TimerProvider implements SimulationTimer {
		@Override
		public void notifyAt(long time, TimerEventListener listener, DEECoContainer container) {
			// Bind listener to host id
			binding.put(container.getId(), listener);
			
			// Do the native register
			OMNeTNative.nativeCallAt(OMNeTNative.timeToOmnet(time), container.getId());
		}

		@Override
		public long getCurrentMilliseconds() {
			double time = OMNeTNative.nativeGetCurrentTime();
			if (time < 0) {
				time = 0;
			}
			return OMNeTNative.timeFromOmnet(time);
		}

		@Override
		public void start(long duration) {
			OMNeTSimulation.this.start(duration);
		}
	}

	class Host implements OMNeTNativeListener {
		final int id;
		
		

		Host(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		@Override
		public void at(double absoluteTime) {
			// Invoke the listener
			binding.get(id).at(OMNeTNative.timeFromOmnet(absoluteTime));
		}

		@Override
		public void packetReceived(byte[] packet, double rssi) {
			System.out.println("Native host: Packet received");			
		}
	}

	private final Map<Integer, Host> hosts = new HashMap<Integer, OMNeTSimulation.Host>();

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
		if (!hosts.containsKey(container.getId())) {
			Host host = new Host(container.getId());
			hosts.put(host.getId(), host);
			OMNeTNative.nativeRegister(host, host.getId());
			System.out.println("Registered host " + host.getId());
		} else {
			throw new UnsupportedOperationException("Host with this id is already registered");
		}
	}

	public void start(long duration) {
		OMNeTNative.nativeRun("Cmdenv", "omnetpp.ini");
	}
}
