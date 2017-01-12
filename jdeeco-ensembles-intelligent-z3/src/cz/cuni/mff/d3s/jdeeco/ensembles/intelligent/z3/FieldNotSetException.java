package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

public class FieldNotSetException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FieldNotSetException(String string) {
		super("The field " + string + " has the default value null and could not be used. Did you forget to initialize it?");		
	}
}
