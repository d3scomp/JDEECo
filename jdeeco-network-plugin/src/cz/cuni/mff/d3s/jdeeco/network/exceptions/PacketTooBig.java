package cz.cuni.mff.d3s.jdeeco.network.exceptions;

import cz.cuni.mff.d3s.deeco.runtime.DEECoRuntimeException;

/**
 * Packet too big exception
 * 
 * This exception is used to signal that source data cannot be encapsulated in packet due to its size,offset,... 
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class PacketTooBig extends DEECoRuntimeException {
	private static final long serialVersionUID = -5050743690956893670L;

	/**
	 * Crates packet too big exception
	 * 
	 * @param message Detailed problem message
	 */
	public PacketTooBig(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
