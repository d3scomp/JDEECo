package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.path.grammar.PathGrammar;

public enum ETriggerType {
	MEMBER, COORDINATOR, KNOWLEDGE;

	public static ETriggerType fromString(String strRecipient) {
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
