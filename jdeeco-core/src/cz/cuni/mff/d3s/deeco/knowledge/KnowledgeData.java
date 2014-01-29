package cz.cuni.mff.d3s.deeco.knowledge;

import java.io.Serializable;

public class KnowledgeData implements Serializable {
	private final ValueSet knowledge;
	private final String ownerId;
	
	public KnowledgeData(String ownerId, ValueSet knowledge) {
		this.ownerId= ownerId;
		this.knowledge = knowledge;
	}

	public ValueSet getKnowledge() {
		return knowledge;
	}

	public String getOwnerId() {
		return ownerId;
	}
}
