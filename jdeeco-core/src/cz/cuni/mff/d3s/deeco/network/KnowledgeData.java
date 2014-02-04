package cz.cuni.mff.d3s.deeco.network;

import java.io.Serializable;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;

/**
 * Class representing a container for sending knowledge values and related
 * metadata over the network.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 */
@SuppressWarnings("serial")
public class KnowledgeData implements Serializable {
	private final ValueSet knowledge;
	private final String componentId;
	private final long versionId;
	private final String sender;
	
	public KnowledgeData(String componentId, ValueSet knowledge, long versionId, String sender) {
		this.componentId= componentId;
		this.knowledge = knowledge;
		this.versionId = versionId;
		this.sender = sender;
	}

	public ValueSet getKnowledge() {
		return knowledge;
	}

	public String getComponentId() {
		return componentId;
	}

	public long getVersionId() {
		return versionId;
	}

	public String getSender() {
		return sender;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((componentId == null) ? 0 : componentId.hashCode());
		result = prime * result
				+ ((knowledge == null) ? 0 : knowledge.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + (int) (versionId ^ (versionId >>> 32));
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
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (versionId != other.versionId)
			return false;
		return true;
	}

		
	
}
