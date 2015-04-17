package cz.cuni.mff.d3s.jdeeco.network.omnet;

import java.io.File;
import java.io.IOException;
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
import cz.cuni.mff.d3s.jdeeco.network.address.IPAddress;

public class OMNeTSimulation implements DEECoPlugin {
	public class Timer implements SimulationTimer {
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
			try {
				File config = OMNeTSimulation.this.getOmnetConfig(duration);
				OMNeTNative.nativeRun("Cmdenv", config.getAbsolutePath());
			} catch (IOException e) {
				System.err.println("Failed to start simulation: " + e.getMessage());
			}
		}
	}

	class OMNeTHost implements OMNeTNativeListener {
		public final DEECoContainer container;
		
		TimerEventListener eventListener = null;
		OMNeTBroadcastDevice broadcastDevice = null;
		OMNeTInfrastructureDevice infrastructureDevice = null;
		
		OMNeTHost(DEECoContainer container) {
			this.container = container;
		}

		public int getId() {
			return container.getId();
		}
		
		public void setEventListener(long time, TimerEventListener listener) {
			// Register listener and schedule event
			eventListener = listener;
			OMNeTNative.nativeCallAt(OMNeTNative.timeToOmnet(time), getId());
		}
		
		public void setBroadcastDevice(OMNeTBroadcastDevice device) {
			broadcastDevice = device;
		}
		
		public void setInfrastructureDevice(OMNeTInfrastructureDevice device) {
			infrastructureDevice = device;
		}
		
		public void sendInfrastructurePacket(byte[] packet, IPAddress address) {
			OMNeTNative.nativeSendPacket(getId(), packet, address.ipAddress/*"MANET.node[" + address.ipAddress + "]"*/);
		}
		
		public void sendBroadcastPacket(byte[] packet) {
			OMNeTNative.nativeSendPacket(getId(), packet, "");
		}

		@Override
		public void at(double absoluteTime) {
			eventListener.at(OMNeTNative.timeFromOmnet(absoluteTime));
		}

		@Override
		public void packetReceived(byte[] packet, double rssi) {
			if(rssi == -1 && infrastructureDevice != null) {
				infrastructureDevice.receivePacket(packet);
			}
			
			if(rssi >= 0 && broadcastDevice != null) {
				broadcastDevice.receivePacket(packet, rssi);
			}
		}
	}
	
	private final Map<Integer, OMNeTHost> hosts = new HashMap<Integer, OMNeTSimulation.OMNeTHost>();
	private Timer timeProvider = new Timer();
	
	public File getOmnetConfig(long limit) throws IOException {
		OMNeTConfigGenerator generator = new OMNeTConfigGenerator(limit);
		
		for(OMNeTHost host: hosts.values()) {
			generator.addNode(host);
		}
		
		return generator.writeToOmnet();
	}

	public SimulationTimer getTimer() {
		return timeProvider;
	}
	
	public OMNeTHost getHost(int id) {
		return hosts.get(id);
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return new LinkedList<Class<? extends DEECoPlugin>>();
	}

	@Override
	public void init(DEECoContainer container) {
		if (!hosts.containsKey(container.getId())) {
			OMNeTHost host = new OMNeTHost(container);
			hosts.put(host.getId(), host);
			OMNeTNative.nativeRegister(host, host.getId());
			System.out.println("Registered host " + host.getId());
		} else {
			throw new UnsupportedOperationException("Host with this id is already registered");
		}
	}
}
