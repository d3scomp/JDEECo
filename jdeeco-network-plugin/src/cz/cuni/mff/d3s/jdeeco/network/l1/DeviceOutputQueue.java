package cz.cuni.mff.d3s.jdeeco.network.l1;

import java.nio.ByteBuffer;

import cz.cuni.mff.d3s.jdeeco.network.Address;
import cz.cuni.mff.d3s.jdeeco.network.Device;

public class DeviceOutputQueue {
	
	public final Device device;
	public final Address address;
	
	private final byte [] l0Packet;
	private int l0PacketSize;
	
	public DeviceOutputQueue(Device device, Address address) {
		this.device = device;
		this.address = address;
		this.l0Packet = new byte [device.getMTU()];
	}
	
	public int availableL0Space() {
		return l0Packet.length - l0PacketSize;
	}
	
	public boolean sendImmidiate(L1Packet packet) {
		byte [] l1PacketBytes = packet.getBytes();
		//L1 Packet is too big to fit into the device MTU.
		if (l1PacketBytes.length > l0Packet.length) {
			return false;
		}
		fillL0Packet(l1PacketBytes);
		send();
		return true;
	}
	
	public boolean sendDelayed(L1Packet packet) {
		byte [] l1PacketBytes = packet.getBytes();
		//L1 Packet is too big to fit into the device MTU.
		if (l1PacketBytes.length > l0Packet.length) {
			return false;
		}
		fillL0Packet(l1PacketBytes);
		if (l0PacketSize == l0Packet.length) {
			send();
		} else {
			//TODO add scheduler task
		}
		return true;
	}
	
	//------------- PRIVATE METHODS --------------------
	
	private void send() {
		if (l0PacketSize > 0) {
			device.send(l0Packet, address);
			l0PacketSize = 0;
		}
	}
	
	private void fillL0Packet(byte [] bytes) {
		//L1 packet is too big to fit into the remaining L0 packet space.
		if (bytes.length > availableL0Space()) {
			//Send the current L0 packet to release L0 space.
			send();
		}
		ByteBuffer byteBuffer = ByteBuffer.wrap(l0Packet);
		byteBuffer.position(l0PacketSize);
		byteBuffer.put(bytes);
		l0PacketSize += bytes.length;
	}
	
	
}
