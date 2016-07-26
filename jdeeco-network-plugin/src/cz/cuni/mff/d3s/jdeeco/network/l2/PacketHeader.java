package cz.cuni.mff.d3s.jdeeco.network.l2;


/**
 * Layer 2 packet header
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class PacketHeader {
	/**
	 * Type of L2 packet
	 */
	public L2PacketType type;

	/**
	 * Encodes header into byte array
	 * 
	 * @return Byte encoded packet header
	 */
	public byte[] marshall() {
		byte[] data = new byte[1];
		data[0] = type.getValue();
		return data;
	}

	/**
	 * Creates L2 packet header
	 * 
	 * @param packetType
	 *            Type of packet
	 */
	public PacketHeader(L2PacketType packetType) {
		this.type = packetType;
	}
}
