package cz.cuni.mff.d3s.deeco.network;

public interface NetworkInterface {
	public String getHostId();
	public void sendPacket(byte[] packet, String recipient);
	public void packetReceived(byte[] packet, double rssi);
}
