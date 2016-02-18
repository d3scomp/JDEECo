package cz.cuni.mff.d3s.jdeeco.network.l2;

import java.util.*;

import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;

/**
 * Information about received L2 packet
 * 
 * used to decide about packet by Layer 2 strategies
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class L2ReceivedInfo {
	/**
	 * Source L1 packets
	 */
	public final Collection<L1Packet> srcFragments;

	/**
	 * Source jDEECo node
	 */
	public final byte srcNode;

	/**
	 * L2 Packet identification
	 * 
	 * Used to determine how L1 packets should be combined into L2. Should be small and unique in combination with
	 * srcNode.
	 */
	public final int dataId;

	/**
	 * Constructs ReceivedInfo
	 * 
	 * @param srcFragments
	 *            Source L1 packets used to construct L2 packets
	 * @param srcNode
	 *            Source node
	 * @param dataId
	 *            Source data identification (Unique to L2 packet)
	 */
	public L2ReceivedInfo(Collection<L1Packet> srcFragments, byte srcNode, int dataId) {
		this.srcFragments = srcFragments;
		this.srcNode = srcNode;
		this.dataId = dataId;
	}
}
