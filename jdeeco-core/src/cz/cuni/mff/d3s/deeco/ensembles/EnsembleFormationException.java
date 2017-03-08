package cz.cuni.mff.d3s.deeco.ensembles;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
/**
 * Exception indicating that the ensemble formation has failed significantly. The exact reason is 
 * implementation specific and should be attached as cause. Used in {@link EnsembleFactory#createInstances}.
 * 
 * @author Filip Krijt
 */
public class EnsembleFormationException extends DEECoException {

	private static final long serialVersionUID = 1L;	

	public EnsembleFormationException(Throwable cause) {
		super(cause);		
	}

	public EnsembleFormationException(String message, Throwable cause) {
		super(message, cause);		
	}
}
