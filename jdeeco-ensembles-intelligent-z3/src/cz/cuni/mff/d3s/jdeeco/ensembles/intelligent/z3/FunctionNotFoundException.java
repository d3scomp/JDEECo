package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

public class FunctionNotFoundException extends RuntimeException {

	public FunctionNotFoundException(String name) {
		super("The function " + name + " either does not exist or cannot be used in this context. Did you use a general function in a constraint or fitness?");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

}
