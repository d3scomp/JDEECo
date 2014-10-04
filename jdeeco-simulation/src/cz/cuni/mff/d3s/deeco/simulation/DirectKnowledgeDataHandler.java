/**
 * 
 */
package cz.cuni.mff.d3s.deeco.simulation;

import java.util.Collection;
import java.util.List;

import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataReceiver;

/**
 * @author Michal
 * 
 */
public class DirectKnowledgeDataHandler extends NetworkKnowledgeDataHandler {

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.network.KnowledgeDataSender#broadcastKnowledgeData
	 * (java.util.List)
	 */
	@Override
	public void networkBroadcast(AbstractHost from, List<? extends KnowledgeData> knowledgeData, Collection<KnowledgeDataReceiver> receivers) {
		for (KnowledgeDataReceiver receiver: receivers) {
			receiver.receive(knowledgeData);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.network.KnowledgeDataSender#sendKnowledgeData(java
	 * .util.List, java.lang.String)
	 */
	@Override
	public void networkSend(AbstractHost from, List<? extends KnowledgeData> knowledgeData, KnowledgeDataReceiver recipient) {
		recipient.receive(knowledgeData);
	}
}
