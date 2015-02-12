package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;

public class NoExecutorAvailableException extends DEECoException {

	private static final long serialVersionUID = 1L;

	public NoExecutorAvailableException() {
		super("No executor was available for the scheduler.");
	}

}
