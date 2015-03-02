package cz.cuni.mff.d3s.jdeeco.network.l2;

/**
 * Strategy for processing L2 packets
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface L2Strategy {
	/**
	 * Processes packet received by L2 layer
	 * 
	 * @param packet Packet to be processed
	 */
	void processL2Packet(L2Packet packet);
}
