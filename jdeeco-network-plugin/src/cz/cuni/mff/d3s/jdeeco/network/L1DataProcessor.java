package cz.cuni.mff.d3s.jdeeco.network;

import cz.cuni.mff.d3s.jdeeco.network.l2.L2ReceivedInfo;

/**
 * Interface for passing L2 packets to Layer 2 from Layer 1
 * XXX TB: Is it really L2 packets?
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface L1DataProcessor {
	/**
	 * Process data received from Layer 1 by Layer 2 strategies
	 * 
	 * This creates a new L2 packet and passes it to Layer 2
	 * XXX TB: Creating an L2 packet is not part of this contract, thus it should not be stated here. It belongs to the implementation of the interface.
	 * 
	 * @param data
	 *            Packet data
	 * @param receivedInfo
	 *            Packet received information
	 */
	public void processL1Data(byte[] data, L2ReceivedInfo receivedInfo);
}
