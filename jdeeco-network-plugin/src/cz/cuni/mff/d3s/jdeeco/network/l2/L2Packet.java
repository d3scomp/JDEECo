package cz.cuni.mff.d3s.jdeeco.network.l2;

import java.util.Arrays;

/**
 * Layer 2 jDEECo network packet
 * 
 * Layer 2 network packet has two representations. It can be represented as Object holding structured data, or byte
 * array holding the marshaled version of the data. The packet provides methods that handle obtaining these two versions
 * of content (getObject for structured data and getData for byte array). When the packet is create one of these two
 * versions of data has to be provided. The second one is then created from the first one using
 * marshallers/unmarshallers provided by layer 2 network implementation. The calls to getObjetc and getData cannot be
 * performed when the reference to Layer 2 network haven't been set previously by call to setLayer method. Once both
 * packet data representations are in place (one was provided to the constructor, the second one was generated from the
 * first one) no more conversions are performed as the generated data are cached (the packet internal data cannot be
 * changed).
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class L2Packet {
	// Reference to layer needed for marshalling/unmarshalling packet content
	private Layer2 l2Layer;
	public final PacketHeader header;
	public final L2ReceivedInfo receivedInfo;

	// Marshalled packet data
	private byte[] data;

	// Unmarshalled packet data
	private Object object;

	/**
	 * Sets reference to Layer 2
	 * 
	 * This reference is needed before the packet can be marshalled/unmarshaled.
	 * 
	 * @see getObject
	 * @see getData
	 * 
	 * @param layer
	 *            Layer 2 to be used for marshalling/unmarshalling this packet
	 */
	void setLayer(Layer2 layer) {
		l2Layer = layer;
	}

	/**
	 * Creates L2 packet from object
	 * 
	 * This version of the constructor is supposed to be used when data from upper layers are passed to network layer
	 * (which starts with Layer2)
	 * 
	 * @param object
	 *            Source object to be stored in packet
	 */
	public L2Packet(PacketHeader header, Object object) {
		this.header = header;
		this.receivedInfo = null;
		this.object = object;
	}

	/**
	 * Creates L2 packet from binary data
	 * 
	 * This version of the constructor is supposed to be used when data are entering Layer 2 from Layer 1.
	 * 
	 * @see Layer1
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
	 * The resulting object is generated on demand from binary data and cached. The call to this method has to be
	 * preceded by setting Layer 2 reference by setLayer method.
	 * 
	 * @see setLayer
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
	 * The resulting binary data are created on demand and cached. The call to this method has to be preceded by setting
	 * Layer 2 reference by setLayer method.
	 * 
	 * @see setLayer
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
	protected static byte[] createL2PacketData(PacketHeader header, byte[] data) {
		byte[] headerData = header.marshall();
		byte[] packet = new byte[data.length + headerData.length];
		System.arraycopy(headerData, 0, packet, 0, headerData.length);
		System.arraycopy(data, 0, packet, headerData.length, data.length);
		return packet;
	}
}
