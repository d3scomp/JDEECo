package cz.cuni.mff.d3s.deeco.simulation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.DataReceiver;
import cz.cuni.mff.d3s.deeco.network.DataSender;

@SuppressWarnings("rawtypes")
public abstract class NetworkDataHandler implements DataReceiversHolder {

	protected static final double DEFAULT_MANET_RSSI = 0.5;
	protected static final double DEFAULT_IP_RSSI = -1.0;
	
	protected final Map<AbstractHost, Collection<DataReceiver>> receivers;
	protected final Map<AbstractHost, DataSender> senders;

	public NetworkDataHandler() {
		this.receivers = new HashMap<>();
		this.senders = new HashMap<>();
	}
	
	@Override
	public void addDataReceiver(AbstractHost host,
			DataReceiver<?> receiver) {
		Collection<DataReceiver> hostReceivers = receivers.get(host);
		if (hostReceivers == null) {
			hostReceivers = new HashSet<>();
			receivers.put(host, hostReceivers);
		}
		hostReceivers.add(receiver);
	}
	
	public DataSender getDataSender(AbstractHost host) {
		DataSender result = senders.get(host);
		if (result == null) {
			result = new DataSenderImpl(host);
			senders.put(host, result);
		}
		return result;
	}
	
	protected abstract void networkBroadcast(AbstractHost from, Object data, Map<AbstractHost, Collection<DataReceiver>> receivers);
	protected abstract void networkSend(AbstractHost from, Object data, AbstractHost recipientHost, Collection<DataReceiver> recipientReceivers);
	
	private class DataSenderImpl implements DataSender {
		
		private final AbstractHost host;

		public DataSenderImpl(AbstractHost host) {
			this.host = host;
		}
		
		@Override
		public void broadcastData(Object data) {
			Map<AbstractHost, Collection<DataReceiver>> sendTo = new HashMap<AbstractHost, Collection<DataReceiver>>(receivers);
			sendTo.remove(host);
			networkBroadcast(host, data, sendTo);	
		}

		@Override
		public void sendData(Object data, String recipient) {
			for (AbstractHost host : receivers.keySet()) {
				if (host.getHostId().equals(recipient)) {
					networkSend(this.host, data, host, receivers.get(host));
					break;
				}
			}
			
		}
		
	}

}
