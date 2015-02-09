package cz.cuni.mff.d3s.jdeeco.network.l1;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.jdeeco.network.Address;
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

	public boolean sendL2Packet(L2Packet l2Packet, Address address) {
		if (l2Packet != null) {
			for (Device device : devices) {
				if (device.canSend(address)) {
					List<L1Packet> l1Packets = dissassembleL2ToL1(l2Packet, device.getMTU());
					for (L1Packet l1Packet : l1Packets) {
						device.send(L1ToL0Packet(l1Packet), address);
					}
					return true;
				}
			}
		}
		return false;
	}

	public boolean sendL1Packet(L1Packet l1Packet, Address address) {
		if (l1Packet != null) {
			for (Device device : devices) {
				if (device.canSend(address)) {
					device.send(L1ToL0Packet(l1Packet), address);
					return true;
				}
			}
		}
		return false;
	}

	public L1Packet processL0Packet(byte[] l0Packet, Address srcAddress) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(l0Packet.length);
		int totalSize = byteBuffer.getInt(0);
		int payloadSize = byteBuffer.get(4);
		int startPos = byteBuffer.getInt(8);
		int dataId = byteBuffer.getInt(12);
		int srcNode = byteBuffer.getInt(16);
		byte [] payload = Arrays.copyOfRange(l0Packet, 20, l0Packet.length);
		return new L1Packet(payload, srcNode, dataId, startPos, payloadSize, totalSize, new L1ReceivedInfo(srcAddress));
	}

	/**
	 * Retrieves the byte array representation of the L1 packet.
	 * 
	 * @param l1Packet
	 *            packet to be encoded
	 * @return array of bytes encoding this L1 packet
	 */
	protected byte [] L1ToL0Packet(L1Packet l1Packet) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(L1Packet.HEADER_SIZE + l1Packet.payloadSize);
		byteBuffer.putInt(l1Packet.totalSize);
		byteBuffer.putInt(l1Packet.payloadSize);
		byteBuffer.putInt(l1Packet.startPos);
		byteBuffer.putInt(l1Packet.dataId);
		byteBuffer.putInt(l1Packet.srcNode);
		byteBuffer.put(l1Packet.payload);
		return byteBuffer.array();
	}

	protected List<L1Packet> dissassembleL2ToL1(L2Packet l2Packet, int mtu) {
		LinkedList<L1Packet> result = new LinkedList<L1Packet>();
		if (l2Packet.getData() != null && l2Packet.getData().length > 0) {
			L2ReceivedInfo receivedInfo = l2Packet.receivedInfo;
			int totalSize = l2Packet.getData().length;
			int srcNode, dataId;
			if (receivedInfo == null) {
				srcNode = nodeId;
				dataId = dataIdSource.createDataID();
			} else {
				srcNode = receivedInfo.srcNode;
				dataId = receivedInfo.dataId;
			}
			int current = 0;
			byte[] payload;
			while (current < l2Packet.getData().length) {
				payload = Arrays.copyOfRange(l2Packet.getData(), current,
						Math.min(current + mtu, l2Packet.getData().length - 1));
				result.add(new L1Packet(payload, srcNode, dataId, current, payload.length, totalSize, null));
				current += mtu;
			}
		}
		return result;
	}
}
