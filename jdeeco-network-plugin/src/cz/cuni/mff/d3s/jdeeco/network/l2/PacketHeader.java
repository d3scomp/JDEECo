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
	private byte packetType;

	/**
	 * Creates L2 packet header
	 * 
	 * @param packetType
	 *            Type of packet
	 */
	public PacketHeader(byte packetType) {
		this.packetType = packetType;
	}

	/**
	 * Gets packet type
	 * 
	 * @return packet type
	 */
	public byte getPacketType() {
		return packetType;
	}
}
