/**
 * 
 */
package cz.cuni.mff.d3s.deeco.simulation;

import java.util.Collection;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.DataReceiver;

/**
 * @author Michal
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DirectKnowledgeDataHandler extends NetworkDataHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.network.KnowledgeDataSender#broadcastKnowledgeData
	 * (java.util.List)
	 */
	@Override
	public void networkBroadcast(AbstractHost from, Object data,
			Map<AbstractHost, Collection<DataReceiver>> receivers) {
		for (Collection<DataReceiver> innerReceivers : receivers.values()) {
			for (DataReceiver receiver: innerReceivers) {
				receiver.checkAndReceive(data, DEFAULT_MANET_RSSI);
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
	public void networkSend(AbstractHost from, Object data,
			AbstractHost recipientHost,
			Collection<DataReceiver> recipientReceivers) {
		for (DataReceiver receiver : recipientReceivers) {
			receiver.checkAndReceive(data, DEFAULT_IP_RSSI);
		}
	}
}
