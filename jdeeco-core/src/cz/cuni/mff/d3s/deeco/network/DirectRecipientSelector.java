package cz.cuni.mff.d3s.deeco.network;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;

public interface DirectRecipientSelector {

	public Collection<String> getRecipients(KnowledgeData data, ReadOnlyKnowledgeManager sender);
}
