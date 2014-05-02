package cz.cuni.mff.d3s.deeco.simulation;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.network.NetworkInterface;
import cz.cuni.mff.d3s.deeco.network.NetworkProvider;

public class DirectConnectionsProvider implements NetworkProvider {

	private final Map<String, NetworkInterface> network;
	
	public DirectConnectionsProvider() {
		this.network = new HashMap<String, NetworkInterface>();
	}
	
	@Override
	public void registerInNetwork(NetworkInterface hostInterface,
			String networkId) {
		network.put(networkId, hostInterface);

	}

	@Override
	public void sendPacket(String fromId, byte[] data, String recipient) {
		if (recipient == null || recipient.isEmpty()) {
			for (NetworkInterface networkInterface: network.values()) {
				networkInterface.sendPacket(data, recipient);
			}
		} else {
			NetworkInterface networkInterface = network.get(recipient);
			if (networkInterface != null)
				networkInterface.sendPacket(data, recipient);
		}
	}

}
