package cz.cuni.mff.d3s.jdeeco.network.l1;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.jdeeco.network.Address;
import cz.cuni.mff.d3s.jdeeco.network.l0.Device;
import cz.cuni.mff.d3s.jdeeco.network.l0.Layer0;
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

	private final Set<L1Strategy> strategies;
	private final int nodeId;
	private final DataIDSource dataIdSource;
	private final Map<Device, Layer0> layers0;
	private final Map<CollectorKey, Collector> collectors;

	public Layer1(int nodeId, DataIDSource dataIdSource) {
		this.layers0 = new HashMap<Device, Layer0>();
		this.strategies = new HashSet<L1Strategy>();
		this.collectors = new HashMap<CollectorKey, Collector>();
		this.nodeId = nodeId;
		this.dataIdSource = dataIdSource;
	}

	public void registerStrategy(L1Strategy strategy) {
		strategies.add(strategy);
	}

	public void registerDevice(Device device) {
		if (!layers0.containsKey(device)) {
			layers0.put(device, new Layer0(device));
		}
	}

	public boolean sendL2Packet(L2Packet l2Packet, Address address) {
		if (l2Packet != null) {
			for (Device device : layers0.keySet()) {
				/**
				 * Go through every device and check whether it is capable to send to the desired address.
				 */
				if (device.canSend(address)) {
					int chunkSize = device.getMTU() - 4;
					/**
					 * Disassemble the L2 packet into the L1 packets.
					 */
					List<L1Packet> l1Packets = dissassembleL2ToL1(l2Packet, chunkSize);
					if (l1Packets.size() > 0) {
						sendOrBuffer(l1Packets, device, address);
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}

	public boolean sendL1Packet(L1Packet l1Packet, Address address) {
		if (l1Packet != null) {
			for (Device device : layers0.keySet()) {
				if (device.canSend(address)) {
					send(l1Packet, device, address);
					return true;
				}
			}
		}
		return false;
	}

	protected void send(L1Packet l1Packet, Device device, Address address) {
		Layer0 layer0 = layers0.get(device);
		layer0.bufferPackets(l1Packet, address);
		layer0.sendAll();
	}

	protected void sendOrBuffer(Collection<L1Packet> l1Packets, Device device, Address address) {
		Layer0 layer0 = layers0.get(device);
		layer0.bufferPackets(l1Packets, address);
		layer0.sendMTUs();
	}

	public void processL0Packet(byte[] l0Packet, Device device, ReceivedInfo receivedInfo) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(l0Packet);
		int l1PacketCount = byteBuffer.getInt();
		int l0PacketChunkSize;
		byte[] l0PacketChunk;
		L1Packet l1Packet;
		Collector collector = null;
		CollectorKey key = null;
		for (int i = 0; i < l1PacketCount; i++) {
			l0PacketChunkSize = byteBuffer.getInt();
			l0PacketChunk = new byte[l0PacketChunkSize];
			byteBuffer.get(l0PacketChunk, byteBuffer.position(), l0PacketChunkSize);
			l1Packet = L1Packet.fromBytes(l0PacketChunk);
			l1Packet.receivedInfo = receivedInfo;
			if (key == null || key.equals(new CollectorKey(l1Packet.dataId, l1Packet.srcNode))) {
				key = new CollectorKey(l1Packet.dataId, l1Packet.srcNode);
				collector = collectors.get(key);
				if (collector == null) {
					collector = new Collector(l1Packet.totalSize);
					collectors.put(key, collector);
				}
			}
			collector.addL1Packet(l1Packet);
			if (collector.isComplete()) {
				//TODO notify l2
			}
		}
	}

	protected List<L1Packet> dissassembleL2ToL1(L2Packet l2Packet, int mtu) {
		LinkedList<L1Packet> result = new LinkedList<L1Packet>();
		if (l2Packet.getData() != null && l2Packet.getData().length > 0) {
			L2ReceivedInfo receivedInfo = l2Packet.receivedInfo;
			int totalSize = l2Packet.getData().length;
			int srcNode, dataId;
			int chunkSize = mtu - Layer0.L0_CHUNK_COUNT_BYTES - Layer0.L0_CHUNK_SIZE_BYTES; // MTU - L1PacketCount -
																							// SIZE OF L1Packet
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
						Math.min(current + chunkSize, l2Packet.getData().length - 1));
				result.add(new L1Packet(payload, srcNode, dataId, current, totalSize, null));
				current += chunkSize;
			}
		}
		return result;
	}

	protected class Collector {
		private final LinkedList<L1Packet> l1Packets;
		private final boolean[] map;

		public Collector(int totalSize) {
			this.l1Packets = new LinkedList<L1Packet>();
			this.map = new boolean[totalSize];
		}

		public void addL1Packet(L1Packet l1Packet) {
			this.l1Packets.addAll(l1Packets);
			for (int i = l1Packet.startPos; i < l1Packet.startPos + l1Packet.payloadSize; i++) {
				map[i] = true;
			}
		}

		public boolean isComplete() {
			if (l1Packets.isEmpty()) {
				return false;
			}
			for (boolean fill : map) {
				if (!fill) {
					return false;
				}
			}
			return true;
		}
	}

	protected class CollectorKey {
		public final int dataId;
		public final int srcNode;

		public CollectorKey(int dataId, int srcNode) {
			this.dataId = dataId;
			this.srcNode = srcNode;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + dataId;
			result = prime * result + srcNode;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CollectorKey other = (CollectorKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (dataId != other.dataId)
				return false;
			if (srcNode != other.srcNode)
				return false;
			return true;
		}

		private Layer1 getOuterType() {
			return Layer1.this;
		}
	}
}
