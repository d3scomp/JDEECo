package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;

/**
 * Thrown by {@link ScriptInputVariableRegistry} when heterogeneous array is given as an input variable.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class HeterogeneousArrayException extends DEECoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HeterogeneousArrayException(String message, Throwable cause) {
		super(message, cause);
	}

	public HeterogeneousArrayException(String message) {
		super(message);
	}

	public HeterogeneousArrayException(Throwable cause) {
		super(cause);
	}
	

	
}
