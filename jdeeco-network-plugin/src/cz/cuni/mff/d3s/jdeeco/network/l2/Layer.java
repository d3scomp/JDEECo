package cz.cuni.mff.d3s.jdeeco.network.l2;

import cz.cuni.mff.d3s.jdeeco.network.l1.Address;

/**
 * Interface for L2 methods to be called from layer 1 or knowledge management
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class Layer {
	/**
	 * Processes L2 packet by registered L2 strategies
	 * 
	 * @param packet
	 *            Packet to be processed
	 */
	void processL2Packet(L2Packet packet) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sends L2 Packet to L1
	 * 
	 * TODO: Address is L1 specific ???
	 * 
	 * @param packet
	 *            Packet to be sent
	 * @param address
	 *            destination address for packet
	 */
	void sendL2Packet(L2Packet packet, Address address) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Registers L2 strategy for processing L2 packets
	 * 
	 * @param strategy
	 *            Strategy to register
	 */
	void registerL2Strategy(Strategy strategy) {
		throw new UnsupportedOperationException();
	}
}
