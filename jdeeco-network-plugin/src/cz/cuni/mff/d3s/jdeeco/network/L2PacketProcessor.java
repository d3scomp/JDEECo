package cz.cuni.mff.d3s.jdeeco.network;

import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;

/**
 * Interface for passing L2 packets to Layer 2 from Layer 1
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface L2PacketProcessor {
	/**
	 * Processes L2 packet Layer 2
	 * 
	 * @param packet
	 *            Packet to be processed
	 */
	public void processL2Packet(L2Packet packet);
}
