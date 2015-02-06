package cz.cuni.mff.d3s.jdeeco.network.l2;

/**
 * Layer 2 jDEECo network packet
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class L2Packet {
	private PacketHeader header;
	private ReceivedInfo receivedInfo;
	private byte[] marshalledData;
	private Object object;

	/**
	 * Gets object representation of packet content
	 * 
	 * The resulting object is generated on demand from binary data and cached
	 * 
	 * @return Object representing packet content
	 */
	public Object getObject() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get binary representation of packet content
	 * 
	 * The resulting binary data are created on demand and cached
	 * 
	 * @return Binary data representing packet content
	 */
	public byte[] getMarshalledData() {
		throw new UnsupportedOperationException();
	}
}
