package cz.cuni.mff.d3s.deeco.network;

import cz.cuni.mff.d3s.deeco.runtime.DEECoRuntimeException;

/**
 * Exception signaling that communication boundary evaluation failed
 */
public class CommunicationBoundaryException extends DEECoRuntimeException {
	private static final long serialVersionUID = 1;

	public CommunicationBoundaryException(Throwable e) {
		super(e);
	}
}
