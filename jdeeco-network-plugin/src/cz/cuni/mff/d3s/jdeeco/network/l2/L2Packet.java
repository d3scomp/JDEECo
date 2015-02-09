package cz.cuni.mff.d3s.jdeeco.network.l2;

/**
 * Layer 2 jDEECo network packet
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class L2Packet {
	public final PacketHeader header;
	public final ReceivedInfo receivedInfo;
	
	private byte[] data;
	private Object object;
	private final Layer2 l2Layer;
	
	
	private L2Packet(Layer2 l2Layer, PacketHeader header, ReceivedInfo receivedInfo) {
		this.l2Layer = l2Layer;
		this.header = header;
		this.receivedInfo = receivedInfo;
	}

	/**
	 * Creates L2 packet from object
	 * 
	 * @param l2Layer
	 *            L2 layer
	 * @param object
	 *            Source object to be stored in packet
	 */
	public L2Packet(Layer2 l2Layer, PacketHeader header, Object object) {
		this(l2Layer, header, null);
		this.object = object;
	}
	
	/**
	 * Creates L2 packet from binary data
	 * 
	 * @param l2Layer
	 *            L2 layer
	 * @param data
	 *            Source binary data for object
	 */
	public L2Packet(Layer2 l2Layer, PacketHeader header, byte[] data, ReceivedInfo receivedInfo) {
		this(l2Layer, header, receivedInfo);
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
			object = l2Layer.getMarshallers().unmarshall(header.getPacketType(), data);
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
			data = l2Layer.getMarshallers().marshall(header.getPacketType(), object);
		}
		return data;
	}

	/**
	 * Gets packet received info
	 * 
	 * @return Returns packet received info, or null when no packet received info is available.
	 */
	public ReceivedInfo getReceivedInfo() {
		return receivedInfo;
	}
}
