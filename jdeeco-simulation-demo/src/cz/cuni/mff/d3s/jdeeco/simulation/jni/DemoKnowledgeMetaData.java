package cz.cuni.mff.d3s.jdeeco.simulation.jni;

import cz.cuni.mff.d3s.deeco.network.KnowledgeMetaData;

public class DemoKnowledgeMetaData extends KnowledgeMetaData {

	public int rebroadcastCount = 0;
	
	public DemoKnowledgeMetaData(String componentId, long versionId,
			String sender) {
		super(componentId, versionId, sender);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + rebroadcastCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DemoKnowledgeMetaData other = (DemoKnowledgeMetaData) obj;
		if (rebroadcastCount != other.rebroadcastCount)
			return false;
		return true;
	}

}
