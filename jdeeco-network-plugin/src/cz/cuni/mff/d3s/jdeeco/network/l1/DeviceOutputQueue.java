package cz.cuni.mff.d3s.jdeeco.network.l1;

import java.nio.ByteBuffer;
import java.util.Arrays;

import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.device.Device;

/**
 * 
 * Buffer for outgoing L0 packets.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class DeviceOutputQueue {

	public final Device device;
	public final Address address;

	private final long timeout; // in milliseconds
	private final byte[] l0Packet;
	private int l0PacketSize;

	public DeviceOutputQueue(Device device, Address address, long timeout) {
		this.timeout = timeout;
		this.device = device;
		this.address = address;
		this.l0Packet = new byte[device.getMTU()];
	}

	/**
	 * Calculates remaining space in the L0 packet
	 * 
	 * @return remaining space in L0 packet
	 */
	public int availableL0Space() {
		return l0Packet.length - l0PacketSize;
	}

	/**
	 * Sends the L1 packet immediately.
	 * 
	 * @param packet
	 *            L1 packet to be sent
	 */
	public void sendImmediately(L1Packet packet) {
		fillL0Packet(packet);
		send();
	}

	/**
	 * Buffers the L1 packet and if there is still some space to accommodate other packets it waits until L0 packet is
	 * filled in or the timeout is expired.
	 * 
	 * @param packet
	 */
	public void sendDelayed(L1Packet packet) {
		fillL0Packet(packet);
		if (availableL0Space() < Layer1.MINIMUM_DATA_TRANSMISSION_SIZE) {
			send();
		} else {
			// TODO add scheduler task to send packet
			// TODO: This is temporaly fix
			send();
		}
	}

	// ------------- PRIVATE METHODS --------------------

	protected void send() {
		if (l0PacketSize > 0) {
			byte[] validL0PacketData = Arrays.copyOfRange(l0Packet, 0, l0PacketSize);
			device.send(validL0PacketData, address);
			l0PacketSize = 0;
		}
	}

	protected void fillL0Packet(L1Packet l1Packet) {
		byte[] bytes = l1Packet.getBytes();
		// L1 packet is too big to fit into the remaining L0 packet space.
		if (bytes.length > availableL0Space()) {
			// Send the current L0 packet to release L0 space.
			send();
		}
		ByteBuffer byteBuffer = ByteBuffer.wrap(l0Packet);
		byteBuffer.position(l0PacketSize);
		byteBuffer.put(bytes);
		l0PacketSize += bytes.length;
	}

}
