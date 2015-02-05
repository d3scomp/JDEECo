package cz.cuni.mff.d3s.jdeeco.network.PacketTypes;

public class PacketType {
	final private int value;

	/**
	 * Construct Packet type with associated random int value determined by implementing class name
	 */
	public PacketType() {
		value = name().hashCode();
	}

	/**
	 * Construct Packet type with associated int value determined by implementing class name
	 * 
	 * @param value
	 *            Value to used to identify packet of this type
	 */
	public PacketType(int value) {
		this.value = value;
	}

	/**
	 * Get packet type name
	 * 
	 * @return packet type name
	 */
	public String name() {
		return this.getClass().getName();
	}

	public int value() {
		return value;
	}

	@Override
	public int hashCode() {
		return value();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PacketType) {
			return ((PacketType) obj).value() == value();
		} else {
			return super.equals(obj);
		}
	}
}
