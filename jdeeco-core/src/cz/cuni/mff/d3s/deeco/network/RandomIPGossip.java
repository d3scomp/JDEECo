package cz.cuni.mff.d3s.deeco.network;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public class RandomIPGossip implements IPGossipStrategy {

	//TODO This needs to be changed
	private final Collection<DirectRecipientSelector> recipientSelectors;
	private final DirectGossipStrategy directGossipStrategy;
	
	public RandomIPGossip(Collection<DirectRecipientSelector> recipientSelectors, DirectGossipStrategy directGossipStrategy) {
		this.recipientSelectors = recipientSelectors;
		this.directGossipStrategy = directGossipStrategy;
	}

	@Override
	public Collection<String> getRecipients(KnowledgeData data,
			KnowledgeManager sender) {
		List<String> result = new LinkedList<>();
		
		// Guard for no recipients
		if (recipientSelectors == null)
			return result;

		// Add recipients from all selectors
		for (DirectRecipientSelector selector: recipientSelectors)
			result.addAll(selector.getRecipients(data, sender));
		
		// Filter the owner of the data, remove all
		while (result.remove(data.getMetaData().componentId));
		
		return directGossipStrategy.filterRecipients(result);
	}

}
