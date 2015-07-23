package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

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
