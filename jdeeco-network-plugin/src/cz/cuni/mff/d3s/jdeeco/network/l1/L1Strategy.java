package cz.cuni.mff.d3s.jdeeco.network.l1;

/**
 * Defines L1 strategy
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface L1Strategy {
	/**
	 * Entry point of the L1Strategy. This method is executed whenever L1 packet arrives from network.
	 * 
	 * @param packet
	 *            L1 packet to be processed by this strategy
	 */
	public void processL1Packet(L1Packet packet);
}
