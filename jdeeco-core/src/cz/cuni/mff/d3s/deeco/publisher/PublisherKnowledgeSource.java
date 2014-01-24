package cz.cuni.mff.d3s.deeco.publisher;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;

public interface PublisherKnowledgeSource {
	
	public String getOwnerId();
	public ValueSet getKnowledge();
}
