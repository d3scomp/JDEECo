package cz.cuni.mff.d3s.deeco.network.ip;

import java.util.Collection;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;

/**
 * Provides list of recipient IP addresses for given knowledge data and sender
 * based on ensemble partitioning.
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 */
public class IPGossipClientStrategy extends IPGossipPartitionStrategy {
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.network.IPGossipStrategy#getRecipients(cz.cuni.mff.d3s.deeco.network.KnowledgeData, cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager)
	 */
	@Override
	public Collection<String> getRecipients(KnowledgeData data, KnowledgeManager sender) {
		
		return super.getRecipients(data, sender);
	}
	
	public IPGossipClientStrategy(Set<String> partitions, IPController controller) {
		super(partitions, controller);
	}
}
