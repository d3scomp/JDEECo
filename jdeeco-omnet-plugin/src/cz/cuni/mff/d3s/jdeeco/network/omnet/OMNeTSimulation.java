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
	class OMNeTTimerProvider implements SimulationTimer {
		@Override
		public void notifyAt(long time, TimerEventListener listener, DEECoContainer container) {
			hosts.get(container.getId()).setEventListener(time, listener);
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
			// TODO: Duration ignored
			OMNeTNative.nativeRun("Cmdenv", "omnetpp.ini");
		}
	}

	class OMNeTHost implements OMNeTNativeListener {
		final int id;
		
		TimerEventListener eventListener = null;
		OMNeTBroadcastDevice broadcastDevice = null;
		OMNeTInfrastructureDevice infrastructureDevice = null;
		
		OMNeTHost(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
		
		public void setEventListener(long time, TimerEventListener listener) {
			// Register listener and schedule event
			eventListener = listener;
			OMNeTNative.nativeCallAt(OMNeTNative.timeToOmnet(time), id);
		}
		
		public void setBroadcastDevice(OMNeTBroadcastDevice device) {
			broadcastDevice = device;
		}
		
		public void setInfrastructureDevice(OMNeTInfrastructureDevice device) {
			infrastructureDevice = device;
		}

		@Override
		public void at(double absoluteTime) {
			eventListener.at(OMNeTNative.timeFromOmnet(absoluteTime));
		}

		@Override
		public void packetReceived(byte[] packet, double rssi) {
			System.out.println("Native host: Packet received");
		}
	}
	
	private final Map<Integer, OMNeTHost> hosts = new HashMap<Integer, OMNeTSimulation.OMNeTHost>();
	private OMNeTTimerProvider timeProvider = new OMNeTTimerProvider();

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
			OMNeTHost host = new OMNeTHost(container.getId());
			hosts.put(host.getId(), host);
			OMNeTNative.nativeRegister(host, host.getId());
			System.out.println("Registered host " + host.getId());
		} else {
			throw new UnsupportedOperationException("Host with this id is already registered");
		}
	}
}
