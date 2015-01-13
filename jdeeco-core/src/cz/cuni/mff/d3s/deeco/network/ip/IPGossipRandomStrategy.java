package cz.cuni.mff.d3s.deeco.network.ip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;

/**
 * Provides list of recipient IP addresses for given knowledge data and sender.
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 */
public class IPGossipRandomStrategy extends IPGossipPartitionStrategy {
	
	private Random gen = new Random();
	private int recipientCount;
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.network.IPGossipStrategy#getRecipients(cz.cuni.mff.d3s.deeco.network.KnowledgeData, cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager)
	 */
	@Override
	public Collection<String> getRecipients(KnowledgeData data, KnowledgeManager sender) {
		
		// notice that sender is already excluded from the recipients
		Collection<String> recipients = super.getRecipients(data, sender);
		
		if (recipientCount >= recipients.size())
			return recipients;
		
		// randomly select at most recipientCount addresses
		String[] rec = new String[recipients.size()];
		recipients.toArray(rec);
		ArrayList<String> res = new ArrayList<String>(recipients.size());
		
		for (int i = 0; i < recipientCount; i++) {
			int index = gen.nextInt(recipients.size());
			while (res.contains(rec[index]))
				index++;
			res.add(rec[index]);
		}
		
		return res;
	}
	
	public IPGossipRandomStrategy(int recipientCount, Set<String> partitions, IPController controller) {
		super(partitions, controller);
		this.recipientCount = recipientCount;
	}
}
