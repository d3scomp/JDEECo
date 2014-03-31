package cz.cuni.mff.d3s.deeco.network;

import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;

public interface CommunicationBoundaryPredicate {

	boolean eval(KnowledgeData data, ReadOnlyKnowledgeManager sender);
	
}
