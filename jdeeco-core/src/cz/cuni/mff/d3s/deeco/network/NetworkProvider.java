package cz.cuni.mff.d3s.deeco.network;

import java.util.Collection;

public interface NetworkProvider {
	public Collection<? extends NetworkInterface> getNetworkInterfaces();
	public void registerInNetwork(NetworkInterface hostInterface, String networkId);
	public void sendPacket(String fromId, byte[] data, String recipient);
}
