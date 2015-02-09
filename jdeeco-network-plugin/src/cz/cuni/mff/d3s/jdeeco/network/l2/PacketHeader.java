package cz.cuni.mff.d3s.jdeeco.network.l2;

import cz.cuni.mff.d3s.jdeeco.network.PacketType;

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
	private PacketType packetType;

	/**
	 * Creates L2 packet header
	 * 
	 * @param packetType
	 *            Type of packet
	 */
	public PacketHeader(PacketType packetType) {
		this.packetType = packetType;
	}

	/**
	 * Gets packet type
	 * 
	 * @return packet type
	 */
	public PacketType getPacketType() {
		return packetType;
	}
}
