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
	private final KnowledgeMetaData metaData;
	
	public KnowledgeData(ValueSet knowledge, KnowledgeMetaData metaData) {
		this.knowledge = knowledge;
		this.metaData = metaData;
	}

	public ValueSet getKnowledge() {
		return knowledge;
	}

	public KnowledgeMetaData getMetaData() {
		return metaData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((knowledge == null) ? 0 : knowledge.hashCode());
		result = prime * result
				+ ((metaData == null) ? 0 : metaData.hashCode());
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
		if (knowledge == null) {
			if (other.knowledge != null)
				return false;
		} else if (!knowledge.equals(other.knowledge))
			return false;
		if (metaData == null) {
			if (other.metaData != null)
				return false;
		} else if (!metaData.equals(other.metaData))
			return false;
		return true;
	}
	
	
	
}
