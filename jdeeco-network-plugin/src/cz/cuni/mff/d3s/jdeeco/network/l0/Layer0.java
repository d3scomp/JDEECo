package cz.cuni.mff.d3s.jdeeco.network.l0;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.jdeeco.network.Address;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;

public class Layer0 {

	private static final int L0_CHUNK_SIZE_BYTES = 4;
	private static final int L0_CHUNK_COUNT_BYTES = 4;

	private final Device device;
	private final Map<Address, Set<L1Packet>> l1PacketsByAddress;
	

	public Layer0(Device device) {
		this.l1PacketsByAddress = new HashMap<Address, Set<L1Packet>>();
		this.device = device;
	}

	public Collection<L0PacketL1Packets> getL0PacketsBySize(int l0PacketSize, Collection<L1Packet> l1Packets) {
		Set<L0PacketL1Packets> result = new HashSet<L0PacketL1Packets>();
		int byteSize = getByteSize(l1Packets);
		if (byteSize <= l0PacketSize) {
			result.add(new L0PacketL1Packets(getBytes(l1Packets), l1Packets));
		} else {
			LinkedList<L1Packet> copyOfL1Packets = new LinkedList<L1Packet>(l1Packets);
			LinkedList<L1Packet> partOfL1Packets;
			while (!copyOfL1Packets.isEmpty()) {
				partOfL1Packets = new LinkedList<L1Packet>();
				do {
					partOfL1Packets.add(copyOfL1Packets.pop());
					if (copyOfL1Packets.isEmpty()) {
						break;
					}
				} while (getByteSize(partOfL1Packets) <= l0PacketSize);
				if (!copyOfL1Packets.isEmpty()) {
					copyOfL1Packets.addFirst(partOfL1Packets.removeLast());
				}
				result.add(new L0PacketL1Packets(getBytes(partOfL1Packets), partOfL1Packets));
			}
		}
		return result;
	}
	
	public void sendAll() {
		Iterator<Map.Entry<Address, Set<L1Packet>>> addressIterator = l1PacketsByAddress.entrySet().iterator();
		Map.Entry<Address, Set<L1Packet>> entry;
		while (addressIterator.hasNext()) {
			entry = addressIterator.next();
			Collection<L0PacketL1Packets> l0Packets = getL0PacketsBySize(device.getMTU(), entry.getValue());
			for (L0PacketL1Packets l0Packet: l0Packets) {
				device.send(l0Packet.bytes, entry.getKey());
			}
			addressIterator.remove();
		}
	}
	
	public void sendMTUs() {
		Iterator<Map.Entry<Address, Set<L1Packet>>> addressIterator = l1PacketsByAddress.entrySet().iterator();
		Map.Entry<Address, Set<L1Packet>> entry;
		while (addressIterator.hasNext()) {
			entry = addressIterator.next();
			Collection<L0PacketL1Packets> l0Packets = getL0PacketsBySize(device.getMTU(), entry.getValue());
			for (L0PacketL1Packets l0Packet: l0Packets) {
				if (l0Packet.bytes.length == device.getMTU()) {
					device.send(l0Packet.bytes, entry.getKey());
					entry.getValue().removeAll(l0Packet.l1Packets);
				}
			}
			if (entry.getValue().isEmpty()) {
				addressIterator.remove();
			}
		}
	}
	
	public void bufferPackets(L1Packet l1Packet, Address address) {
		bufferPackets(Arrays.asList(l1Packet), address);
	}
	
	public void bufferPackets(Collection<L1Packet> l1Packets, Address address) {
		Set<L1Packet> bufferredlL1Packets = l1PacketsByAddress.get(address);
		if (l1PacketsByAddress == null) {
			bufferredlL1Packets = new HashSet<L1Packet>();
			l1PacketsByAddress.put(address, bufferredlL1Packets);
		}
		bufferredlL1Packets.addAll(l1Packets);
	}

	private byte[] getBytes(Collection<L1Packet> l1Packets) {
		List<byte[]> l0Chunks = new LinkedList<byte[]>();
		ByteBuffer byteBuffer;
		byte[] l0Chunk;
		int totalSize = 0;
		for (L1Packet l1Packet : l1Packets) {
			l0Chunk = l1Packet.getBytes();
			totalSize += l0Chunk.length + L0_CHUNK_SIZE_BYTES;
			byteBuffer = ByteBuffer.allocate(l0Chunk.length + L0_CHUNK_SIZE_BYTES);
			byteBuffer.putInt(l0Chunk.length);
			byteBuffer.put(l0Chunk);
			l0Chunks.add(byteBuffer.array());
		}
		byteBuffer = ByteBuffer.allocate(totalSize + L0_CHUNK_COUNT_BYTES);
		byteBuffer.putInt(l0Chunks.size());
		for (byte[] bytes : l0Chunks) {
			byteBuffer.put(bytes);
		}
		return byteBuffer.array();
	}

	private int getByteSize(Collection<L1Packet> l1Packets) {
		int result = 0;
		for (L1Packet l1Packet : l1Packets) {
			result += l1Packet.getByteSize() + L0_CHUNK_SIZE_BYTES;
		}
		result += L0_CHUNK_COUNT_BYTES;
		return result;
	}
	
	private class L0PacketL1Packets {
		public final byte [] bytes;
		public final Collection<L1Packet> l1Packets;
		
		public L0PacketL1Packets(byte[] bytes, Collection<L1Packet> l1Packets) {
			super();
			this.bytes = bytes;
			this.l1Packets = l1Packets;
		}
	}
}
