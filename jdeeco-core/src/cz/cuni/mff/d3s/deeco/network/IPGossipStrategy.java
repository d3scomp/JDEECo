package cz.cuni.mff.d3s.deeco.network;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public interface IPGossipStrategy {
	public Collection<String> getRecipients(KnowledgeData data, KnowledgeManager sender);
}
