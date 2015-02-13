package cz.cuni.mff.d3s.jdeeco.network.l2;

import java.util.Arrays;

/**
 * Layer 2 jDEECo network packet
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class L2Packet {
	private Layer2 l2Layer;
	public final PacketHeader header;
	public final L2ReceivedInfo receivedInfo;

	private byte[] data;
	private Object object;

	void setLayer(Layer2 layer) {
		l2Layer = layer;
	}

	private L2Packet(PacketHeader header, L2ReceivedInfo receivedInfo) {
		this.header = header;
		this.receivedInfo = receivedInfo;
	}

	/**
	 * Creates L2 packet from object
	 * 
	 * @param object
	 *            Source object to be stored in packet
	 */
	public L2Packet(PacketHeader header, Object object) {
		this(header, null);
		this.object = object;
	}

	/**
	 * Creates L2 packet from binary data
	 * 
	 * @param packet
	 *            Source binary data for object (1 byte determines packet type, the rest if passed to the matching
	 *            marshaler)
	 */
	public L2Packet(byte[] packet, L2ReceivedInfo receivedInfo) {
		// Split data for packet header and marshaled data
		assert (packet.length > 0);
		final byte type = packet[0];
		final byte[] data = Arrays.copyOfRange(packet, 1, packet.length);

		// Create packet
		this.header = new PacketHeader(new L2PacketType(type));
		this.receivedInfo = receivedInfo;
		this.data = data;
	}

	/**
	 * Gets object representation of packet content
	 * 
	 * The resulting object is generated on demand from binary data and cached
	 * 
	 * @return Object representing packet content
	 */
	public Object getObject() {
		if (object == null) {
			assert (l2Layer != null);
			object = l2Layer.getMarshallers().unmarshall(header.type, data);
		}

		return object;
	}

	/**
	 * Gets binary representation of packet content
	 * 
	 * The resulting binary data are created on demand and cached
	 * 
	 * @return Binary data representing packet content
	 */
	public byte[] getData() {
		if (data == null) {
			assert (l2Layer != null);
			data = l2Layer.getMarshallers().marshall(header.type, object);
		}

		// Combine data and packet type
		return createL2PacketData(header, data);
	}

	/**
	 * Creates packet data from internal data and type
	 * 
	 * @param type
	 *            Type of the packet
	 * @param data
	 *            Internal packet data encoded according to packet type
	 * @return Packet data
	 */
	static byte[] createL2PacketData(PacketHeader header, byte[] data) {
		byte[] headerData = header.marshall();
		byte[] packet = new byte[data.length + headerData.length];
		System.arraycopy(headerData, 0, packet, 0, headerData.length);
		System.arraycopy(data, 0, packet, headerData.length, data.length);
		return packet;
	}
}
