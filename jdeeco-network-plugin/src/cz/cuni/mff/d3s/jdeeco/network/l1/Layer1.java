package cz.cuni.mff.d3s.jdeeco.network.l1;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.jdeeco.network.Device;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2ReceivedInfo;

/**
 * Defines L1 methods that are called from the upper layer (L2) or L1 strategies.
 * 
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class Layer1 {

	private final Set<Strategy> strategies;
	private final Set<Device> devices;
	private final int nodeId;
	private final DataIDSource dataIdSource;

	public Layer1(int nodeId, DataIDSource dataIdSource) {
		this.strategies = new HashSet<Strategy>();
		this.devices = new HashSet<Device>();
		this.nodeId = nodeId;
		this.dataIdSource = dataIdSource;
	}

	public void registerStrategy(Strategy strategy) {
		strategies.add(strategy);
	}

	public void registerDevice(Device device) {
		devices.add(device);
	}

	public boolean sendL2Packet(L2Packet packet, Address address) {
		if (packet != null) {
			for (Device device : devices) {
				if (device.canSend(address)) {
					packet.getData();
					// device.send(encapsulate(packet), address);
					return true;
				}
			}
		}
		return false;
	}

	public boolean sendL1Packet(L1Packet packet, Address address) {
		// TODO
		return false;
	}

	public L1Packet processL0Packet(byte[] packet) {
		// TODO
		return null;
	}

	protected List<L1Packet> dissassembleL2ToL1(L2Packet packet, int mtu) {
		LinkedList<L1Packet> result = new LinkedList<L1Packet>();
		if (packet.getData() != null && packet.getData().length > 0) {
			L2ReceivedInfo receivedInfo = packet.receivedInfo;
			int srcNode, dataId;
			if (receivedInfo == null) {
				srcNode = nodeId;
				dataId = dataIdSource.createDataID();
			} else {
				srcNode = receivedInfo.srcNode;
				dataId = receivedInfo.dataId;
			}
			//L1Packet l1Packet = new L1Packet(packet.getData(), srcNode, dataId, startPos, payloadSize, totalSize, null);
		}
		return result;
	}
}
