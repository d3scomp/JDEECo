package cz.cuni.mff.d3s.deeco.network;

public interface NetworkProvider {
	public void registerInNetwork(NetworkInterface hostInterface, String networkId);
	public void sendPacket(String fromId, byte[] data, String recipient);
}
