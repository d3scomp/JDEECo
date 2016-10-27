package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

/**
 * The MiniZinc scripts support constant identifiers. If one wants to define a constant in the script,
 * then the constant can be referenced using this class, by passing it into {@link ScriptInputVariableRegistry}.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class ScriptIdentifier {

	private String identifierName;
	
	public ScriptIdentifier(String identifierName) {
		this.identifierName = identifierName;
	}
	
	@Override
	public String toString() {
		return identifierName;
	}

}
