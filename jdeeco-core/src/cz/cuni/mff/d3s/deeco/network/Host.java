package cz.cuni.mff.d3s.deeco.network;

import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

/**
 * 
 * Class representing a host in the simulation.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class Host implements CurrentTimeProvider, NetworkInterface {

	private final PacketReceiver packetReceiver;
	private final PacketSender packetSender;
	
	private final String id;
	
	private final NetworkProvider networkProvider;
	private final CurrentTimeProvider timeProvider;
	
	protected Host(NetworkProvider networkProvider, CurrentTimeProvider timeProvider, String jDEECoAppModuleId, boolean hasMANETNic, boolean hasIPNic) {
		this.networkProvider = networkProvider;
		this.timeProvider = timeProvider;
		this.id = jDEECoAppModuleId;
		this.packetReceiver = new PacketReceiver(id);
		this.packetSender = new PacketSender(this);
		this.packetReceiver.setCurrentTimeProvider(this);
	}
	
	public Host(NetworkProvider networkProvider, CurrentTimeProvider timeProvider, String jDEECoAppModuleId) {
		this(networkProvider, timeProvider, jDEECoAppModuleId, true, true);
	}

	public PacketReceiver getPacketReceiver() {
		return packetReceiver;
	}
	
	public PacketSender getPacketSender() {
		return packetSender;
	}

	public String getHostId() {
		return id;
	}

	// Method used by the simulation
	public void packetReceived(byte[] packet, double rssi) {
		packetReceiver.packetReceived(packet, rssi);
	}

	// The method used by publisher
	public void sendPacket(byte[] packet, String recipient) {
		networkProvider.sendPacket(id, packet, recipient);
	}

	@Override
	public long getCurrentMilliseconds() {
		return timeProvider.getCurrentMilliseconds();
	}
	
	public void finalize() {
		packetReceiver.clearCachedMessages();
	}
}
