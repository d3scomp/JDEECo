package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.path.grammar.PathGrammar;

public enum TriggerType {
	MEMBER, COORDINATOR, KNOWLEDGE;

	public static TriggerType fromString(String strRecipient) {
		if (strRecipient != null) {
			if (strRecipient.equals(PathGrammar.COORD))
				return COORDINATOR;
			else if (strRecipient.equals(PathGrammar.MEMBER))
				return MEMBER;
			else
				return KNOWLEDGE;
		}
		return null;
	}
}
