package cz.cuni.mff.d3s.deeco.runners;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;

public class TerminationTimeNotSetException extends DEECoException {

	private static final long serialVersionUID = 1L;

	public TerminationTimeNotSetException(String msg) {
		super(msg);
	}

}
