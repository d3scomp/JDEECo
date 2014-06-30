/**
 * 
 */
package cz.cuni.mff.d3s.deeco.simulation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataSender;

/**
 * @author Michal
 * 
 */
public class DirectKnowledgeDataHandler implements KnowledgeDataSender,
		KnowledgeDataReceiversHolder {

	private final Map<String, KnowledgeDataReceiver> receivers;

	public DirectKnowledgeDataHandler() {
		this.receivers = new HashMap<String, KnowledgeDataReceiver>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.network.KnowledgeDataSender#broadcastKnowledgeData
	 * (java.util.List)
	 */
	@Override
	public void broadcastKnowledgeData(String from,
			List<? extends KnowledgeData> knowledgeData) {
		for (String id : receivers.keySet()) {
			KnowledgeDataReceiver receiver = receivers.get(id);
			if (!from.equals(id)) {
				receiver.receive(knowledgeData);
			}
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
	public void sendKnowledgeData(List<? extends KnowledgeData> knowledgeData,
			String recipient) {
		KnowledgeDataReceiver receiver = receivers.get(recipient);
		if (receiver != null)
			receiver.receive(knowledgeData);

	}

	@Override
	public void addKnowledgeDataReceiver(String id,
			KnowledgeDataReceiver receiver) {
		receivers.put(id, receiver);
	}

}
