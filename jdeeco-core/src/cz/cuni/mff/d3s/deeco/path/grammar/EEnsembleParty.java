package cz.cuni.mff.d3s.deeco.path.grammar;

public enum EEnsembleParty {
	COORDINATOR, MEMBER, CANDIDATE;
	
	public String toString() {
		if (this.equals(MEMBER))
			return PathGrammar.MEMBER;
		else if (this.equals(CANDIDATE))
			return PathGrammar.CANDIDATE;
		else
			return PathGrammar.COORD;
	}
}
