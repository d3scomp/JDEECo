package cz.cuni.mff.d3s.deeco.simulation;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataSender;

public abstract class NetworkKnowledgeDataHandler implements KnowledgeDataReceiversHolder {

	private final Map<AbstractHost, KnowledgeDataReceiver> receivers;
	private final Map<AbstractHost, KnowledgeDataSender> knowledgeDataSenders;

	public NetworkKnowledgeDataHandler() {
		this.receivers = new HashMap<>();
		this.knowledgeDataSenders = new HashMap<>();
	}
	
	@Override
	public void addKnowledgeDataReceiver(AbstractHost host,
			KnowledgeDataReceiver receiver) {
		receivers.put(host, receiver);
	}
	
	public KnowledgeDataSender getKnowledgeDataSender(AbstractHost host) {
		KnowledgeDataSender result = knowledgeDataSenders.get(host);
		if (result == null) {
			result = new NetworkKnowledgeDataSender(host);
			knowledgeDataSenders.put(host, result);
		}
		return result;
	}
	
	protected abstract void networkBroadcast(AbstractHost from, List<? extends KnowledgeData> knowledgeData, Collection<KnowledgeDataReceiver> receivers);
	protected abstract void networkSend(AbstractHost from, List<? extends KnowledgeData> knowledgeData, KnowledgeDataReceiver recipient);
	
	private class NetworkKnowledgeDataSender implements KnowledgeDataSender {
		
		private final AbstractHost host;

		public NetworkKnowledgeDataSender(AbstractHost host) {
			this.host = host;
		}
		
		@Override
		public void broadcastKnowledgeData(
				List<? extends KnowledgeData> knowledgeData) {
			Collection<KnowledgeDataReceiver> sendTo = new LinkedList<KnowledgeDataReceiver>(receivers.values());
			sendTo.remove(receivers.get(host));
			networkBroadcast(host, knowledgeData, sendTo);
			
		}

		@Override
		public void sendKnowledgeData(
				List<? extends KnowledgeData> knowledgeData, String recipient) {
			for (AbstractHost host : receivers.keySet()) {
				if (host.getHostId().equals(recipient)) {
					networkSend(host, knowledgeData, receivers.get(host));
					break;
				}
			}
			
		}
		
	}

}
