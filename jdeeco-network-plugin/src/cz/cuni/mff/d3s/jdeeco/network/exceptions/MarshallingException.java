package cz.cuni.mff.d3s.jdeeco.network.exceptions;

import cz.cuni.mff.d3s.deeco.runtime.DEECoRuntimeException;

/**
 * Marshaling exception is thrown when the encoding of the object into binary data fails
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class MarshallingException extends DEECoRuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates marshaling exception
	 * 
	 * @param cause
	 *            Exception that failed the marshaling
	 */
	public MarshallingException(Throwable cause) {
		super(cause);
	}
}
