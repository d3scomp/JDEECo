package cz.cuni.mff.d3s.deeco.network.connector;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.network.KnowledgeData;

public interface KnowledgeDataStore {
	public Collection<KnowledgeData> getAllKnowledgeData();
	public void removeAll(Collection<KnowledgeData> knowledgeData);
}
