package cz.cuni.mff.d3s.jdeeco.network.l2;


/**
 * Interface for passing L2 packets to Layer 2 from Layer 1 XXX TB: Is it really L2 packets?
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface L1DataProcessor {
	/**
	 * Process data received from Layer 1 by Layer 2 strategies
	 * 
	 * @param packet
	 *            Layer 2 Packet to process
	 */
	public void processL2Packet(L2Packet packet);
}
