package cz.cuni.mff.d3s.jdeeco.network.exceptions;

/**
 * Marshaling exception is thrown when the encoding of the object into binary data fails
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class MarshallingException extends RuntimeException {
	public MarshallingException(Throwable cause) {
		super(cause);
	}
}
