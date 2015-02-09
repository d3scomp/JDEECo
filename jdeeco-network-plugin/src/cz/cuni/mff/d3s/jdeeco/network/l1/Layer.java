package cz.cuni.mff.d3s.jdeeco.network.l1;

import java.util.HashSet;
import java.util.Set;

import cz.cuni.mff.d3s.jdeeco.network.Device;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;

/**
 * Defines L1 methods that are called from the upper layer (L2) or L1 strategies. 
 * 
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class Layer {
	
	private final Set<Strategy> strategies;
	private final Set<Device> devices;
	
	public Layer() {
		this.strategies = new HashSet<Strategy>();
		this.devices = new HashSet<Device>();
	}
	
	public void registerStrategy(Strategy strategy) {
		strategies.add(strategy);
	}
	
	public void registerDevice(Device device) {
		devices.add(device);
	}
	
	public boolean sendL2Packet(L2Packet packet, Address address) {
		if (packet != null) {
			for (Device device: devices) {
				if (device.canSend(address)) {
					packet.getMarshalledData();
					//device.send(encapsulate(packet), address);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean sendL1Packet(L1Packet packet, Address address) {
		//TODO
	}
	
	public L1Packet processL0Packet(byte [] packet) {
		//TODO
		return null;
	}
	
	protected L1Packet encapsulateL2(L2Packet packet) {
		return new L1Packet(packet.getMarshalledData(), packet., dataId, startPos, payloadSize, totalSize, receivedInfo)
	}
}
