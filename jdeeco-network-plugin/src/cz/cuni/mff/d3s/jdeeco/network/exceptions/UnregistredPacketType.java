package cz.cuni.mff.d3s.jdeeco.network.exceptions;

import cz.cuni.mff.d3s.jdeeco.network.PacketType;

/**
 * Unregistered packet type exception is thrown when the packet type specified is not registered at marshaler registry
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class UnregistredPacketType extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private PacketType value;

	/**
	 * Creates unregistered packet type exception
	 * 
	 * @param value
	 *            Packet type that was not registered
	 */
	public UnregistredPacketType(PacketType value) {
		this.value = value;
	}

	/**
	 * Gets message describing which packet type caused the problem
	 */
	@Override
	public String getMessage() {
		return String.format("Packet type %s is has no registred for marshalling.", value);
	}
}
