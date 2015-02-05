package cz.cuni.mff.d3s.jdeeco.network.exceptions;

/**
 * Unregistered packet type exception is thrown when the packet type specified is not registered at marshaler registry
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class UnregistredPacketType extends RuntimeException {
	private byte value;

	public UnregistredPacketType(byte value) {
		this.value = value;
	}

	@Override
	public String getMessage() {
		return String.format("Packet type %d is has no registred for marshalling.", value);
	}
}
