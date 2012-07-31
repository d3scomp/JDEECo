package cz.cuni.mff.d3s.deeco.path.grammar;

public enum EEnsembleParty {
	COORDINATOR, MEMBER;
	
	public String toString() {
		if (this.equals(MEMBER))
			return PathGrammar.MEMBER;
		else
			return PathGrammar.COORD;
	}
}
