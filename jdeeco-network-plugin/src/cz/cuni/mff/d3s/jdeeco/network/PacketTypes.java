package cz.cuni.mff.d3s.jdeeco.network;

/**
 * This class provides constants for known L2 packet types
 * 
 * NOTE: Constants are bytes, but only 4 lowest bits are used for packet types
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class PacketTypes {
	public static byte KNOWLEDGE = 0;
	public static byte GROUPER = 1;
	public static byte ENSEMBLE_DEFINITION = 2;
}
