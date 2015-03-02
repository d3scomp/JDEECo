package cz.cuni.mff.d3s.jdeeco.network.l1;

import cz.cuni.mff.d3s.jdeeco.network.address.Address;

/**
 * Additional information for a received L1 packet
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class ReceivedInfo {
	public final Address srcAddress; /** packet sending node address */

	public ReceivedInfo(Address srcAddress) {
		this.srcAddress = srcAddress;
	}
}
