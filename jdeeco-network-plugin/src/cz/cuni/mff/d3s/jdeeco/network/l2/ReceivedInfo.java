package cz.cuni.mff.d3s.jdeeco.network.l2;

import java.util.*;

import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;

/**
 * Information about received L2 packet
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class ReceivedInfo {
	/**
	 * Source L1 packets
	 * 
	 * TODO: data type collection, is it OK ???
	 */
	private Collection<L1Packet> srcFragments;

	/**
	 * Source jDEECo node
	 * 
	 * TODO: String is OK ??? Maybe we want it to be small
	 */
	private String srcNode;

	/**
	 * L2 Packet identification
	 * 
	 * Used to determine how L1 packets should be combined into L2. Should be small and unique in combination with
	 * srcNode.
	 */
	private int dataId;
}
