package cz.cuni.mff.d3s.deeco.knowledge;

import java.io.Serializable;

public class KnowledgeData implements Serializable {
	private final ValueSet knowledge;
	private final String componentId;
	
	public KnowledgeData(String componentId, ValueSet knowledge) {
		this.componentId= componentId;
		this.knowledge = knowledge;
	}

	public ValueSet getKnowledge() {
		return knowledge;
	}

	public String getComponentId() {
		return componentId;
	}
}
