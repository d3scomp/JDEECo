package cz.cuni.mff.d3s.jdeeco.network;

import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;

/**
 * Interface for processing L2 packets from Layer 2 by Layer 1
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface L2PacketSender {
	/**
	 * Sends L2 packet by Layer 1
	 * 
	 * @param l2Packet
	 *            Packet to send
	 * @param address
	 *            Destination address
	 * @return True if the packet has been processed, false otherwise
	 */
	public boolean sendL2Packet(L2Packet l2Packet, Address address);
}
