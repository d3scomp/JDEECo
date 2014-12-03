package cz.cuni.mff.d3s.deeco.network.ip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.network.IPGossipStrategy;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataHelper;

/**
 * Base class for {@link IPGossipStrategy} implementations using ensemble partitioning.
 * This strategy selects addresses of all hosts in all partitions of given {@link KnowledgeData}.
 * Sender address is excluded automatically.
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 */
public class IPGossipPartitionStrategy implements IPGossipStrategy {

	protected IPController controller;
	protected Collection<String> partitions;
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.network.IPGossipStrategy#getRecipients(cz.cuni.mff.d3s.deeco.network.KnowledgeData, cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager)
	 */
	@Override
	public Collection<String> getRecipients(KnowledgeData data, KnowledgeManager sender) {
		
		HashSet<String> res = new HashSet<String>();
		
		// get union of all partitions to which current KnowledgeData belongs
		// nodes in these partitions are interested in this knowledge
		for (String part : partitions) {
			// value of partitionBy field
			try {
				Object val = KnowledgeDataHelper.getValue(data, part);
				// example: get IP's of an ensemble partitioned by destination
				// for "Berlin" group
				IPRegister table = controller.getRegister(val);
				res.addAll(table.getAddresses());
			} catch (KnowledgeNotFoundException e) {
			}
		}
		
		// remove sender address, it does not have a sense to send data to itself
		res.remove(sender.getId());
		return res;
	}

	public IPGossipPartitionStrategy(Set<String> partitions, IPController controller) {
		this.partitions = new ArrayList<String>(partitions);
		this.controller = controller;
	}
}
