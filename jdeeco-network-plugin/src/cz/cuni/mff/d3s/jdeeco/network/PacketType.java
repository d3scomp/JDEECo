package cz.cuni.mff.d3s.jdeeco.network;

/**
 * L2 Packet type
 * 
 * Determines pay-load type for L2 packet. Contains 4 bit value, stored as byte, to be used in packets.
 * 
 * Constants for known L2 packet types are provided as static fields.
 * 
 * NOTE: Constants are bytes, but only 4 lowest bits are used for packet types.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class PacketType {
	private final byte value;

	/**
	 * Constructs packet type
	 * 
	 * @param value
	 *            Packet type value, only 4 lowest bits can be specified
	 */
	public PacketType(byte value) {
		if((value & ~0b1111) != 0) {
			throw new UnsupportedOperationException("Value out of range");
		}
		this.value = value;
	}

	/**
	 * Gets packet type value
	 * 
	 * Only 4 lowest bits are valid, the higher bits should be zero
	 * 
	 * @return Packet type value
	 */
	public byte getValue() {
		return value;
	}

	/**
	 * Converts packet type value to string
	 */
	@Override
	public String toString() {
		return String.valueOf(getValue());
	}

	public static PacketType KNOWLEDGE = new PacketType((byte) 0);
	public static PacketType GROUPER = new PacketType((byte) 1);
	public static PacketType ENSEMBLE_DEFINITION = new PacketType((byte) 2);
}
