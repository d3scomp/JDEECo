package cz.cuni.mff.d3s.jdeeco.network.l1;

/**
 * Receival additional information for L1 packet
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class L1ReceivedInfo {
	public final Address srcAddress; /** packet sending node address */

	public L1ReceivedInfo(Address srcAddress) {
		this.srcAddress = srcAddress;
	}
}
