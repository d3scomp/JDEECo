package cz.cuni.mff.d3s.deeco.path.grammar;

public enum EEnsembleParty {
	COORDINATOR, MEMBER, MEMBERS, CANDIDATES;
	
	public String toString() {
		if (this.equals(MEMBER))
			return PathGrammar.MEMBER;
		else if (this.equals(MEMBERS))
			return PathGrammar.MEMBERS;
		else
			return PathGrammar.COORD;
	}
}
