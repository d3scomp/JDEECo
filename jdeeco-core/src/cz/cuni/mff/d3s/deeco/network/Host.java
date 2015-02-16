package cz.cuni.mff.d3s.deeco.network;

import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

/**
 * 
 * Class representing a host in the simulation.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
@SuppressWarnings("rawtypes")
public class Host extends AbstractHost implements NetworkInterface {

	private final PacketReceiver packetReceiver;
	private final PacketSender packetSender;
	
	private final NetworkProvider networkProvider;
	
	protected Host(NetworkProvider networkProvider, CurrentTimeProvider timeProvider, String jDEECoAppModuleId, boolean hasMANETNic, boolean hasIPNic) {
		super(jDEECoAppModuleId, timeProvider);
		this.networkProvider = networkProvider;
		this.packetReceiver = new PacketReceiver(id);
		this.packetSender = new PacketSender(this);
		this.packetReceiver.setCurrentTimeProvider(this);
	}
	
	public Host(NetworkProvider networkProvider, CurrentTimeProvider timeProvider, String jDEECoAppModuleId) {
		this(networkProvider, timeProvider, jDEECoAppModuleId, true, true);
	}
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.network.HostDataHandler#getDataSender()
	 */
	@Override
	public DataSender getDataSender() {
		return packetSender;
	}
	
	@Override
	public void addDataReceiver(DataReceiver dataReceiver) {
		packetReceiver.addDataReceiver(dataReceiver);
	}

	// Method used by the simulation
	public void packetReceived(byte[] packet, double rssi) {
		packetReceiver.packetReceived(packet, rssi);
	}

	// The method used by publisher
	public void sendPacket(byte[] packet, String recipient) {
		networkProvider.sendPacket(id, packet, recipient);
	}
	
	public void finalize() {
		packetReceiver.clearCachedMessages();
	}
}
