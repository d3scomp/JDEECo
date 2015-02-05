package cz.cuni.mff.d3s.jdeeco.network.l1;

/**
 * Receival additional information for L1 packet
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class ReceivedInfo {
	public final String srcAddress; /** packet sending node address */

	public ReceivedInfo(String srcAddress) {
		this.srcAddress = srcAddress;
	}
}
