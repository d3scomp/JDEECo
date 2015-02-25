package cz.cuni.mff.d3s.deeco.runtime;

/**
 * Thrown when the same ensemble class is deployed twice. 
 * @author Filip Krijt <krijt@d3s.mff.cuni.cz>
 *
 */
public class DuplicateEnsembleDefinitionException extends DEECoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateEnsembleDefinitionException(Class ensembleClass) {
		super("Duplicate ensemble definition detected - class "+ ensembleClass + ". (Have you tried to pass a same ensemble class twice?)");
	}
}
