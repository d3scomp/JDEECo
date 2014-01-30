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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((componentId == null) ? 0 : componentId.hashCode());
		result = prime * result
				+ ((knowledge == null) ? 0 : knowledge.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KnowledgeData other = (KnowledgeData) obj;
		if (componentId == null) {
			if (other.componentId != null)
				return false;
		} else if (!componentId.equals(other.componentId))
			return false;
		if (knowledge == null) {
			if (other.knowledge != null)
				return false;
		} else if (!knowledge.equals(other.knowledge))
			return false;
		return true;
	}
	
	
}
