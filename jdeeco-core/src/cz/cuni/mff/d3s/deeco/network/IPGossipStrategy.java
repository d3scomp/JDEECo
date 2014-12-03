package cz.cuni.mff.d3s.deeco.network;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

/**
 * Gives the ability to select recipient of particular {@link KnowledgeData} using a
 * gossip-base protocol. The decision is made based on {@link KnowledgeData} sent and
 * the sender.
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 */
public interface IPGossipStrategy {
	/**
	 * Get a collection of host addresses to which given {@link KnowledgeData} should be
	 * sent directly.
	 * 
	 * @param data Instance of {@link KnowledgeData} to be sent.
	 * @param sender 
	 * @return Collection of IP addresses
	 */
	public Collection<String> getRecipients(KnowledgeData data, KnowledgeManager sender);
}
