package cz.cuni.mff.d3s.deeco.simulation;

import static cz.cuni.mff.d3s.deeco.simulation.OMNetSimulation.timeDoubleToLong;
import cz.cuni.mff.d3s.deeco.network.NetworkInterface;
import cz.cuni.mff.d3s.deeco.network.NetworkProvider;
import cz.cuni.mff.d3s.deeco.network.PacketReceiver;
import cz.cuni.mff.d3s.deeco.network.PacketSender;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

/**
 * 
 * Class representing a host in the simulation.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class Host implements CurrentTimeProvider, NetworkInterface {
	

	private SimulationTimeEventListener timeEventListener = null;

	private final PacketReceiver packetReceiver;
	private final PacketSender packetSender;
	
	private final String id;
	
	private final NetworkProvider networkProvider;
	private final PositionProvider positionProvider;
	private final CurrentTimeProvider timeProvider;
	
	
	protected Host(NetworkProvider networkProvider, PositionProvider positionProvider, CurrentTimeProvider timeProvider, String jDEECoAppModuleId, boolean hasMANETNic, boolean hasIPNic) {
		this.networkProvider = networkProvider;
		this.positionProvider = positionProvider;
		this.timeProvider = timeProvider;
		this.id = jDEECoAppModuleId;
		this.packetReceiver = new PacketReceiver(id);
		this.packetSender = new PacketSender(this);
		this.packetReceiver.setCurrentTimeProvider(this);
	}
	
	public Host(NetworkProvider networkProvider, PositionProvider positionProvider, CurrentTimeProvider timeProvider, String jDEECoAppModuleId) {
		this(networkProvider, positionProvider, timeProvider, jDEECoAppModuleId, true, true);
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

	public void setSimulationTimeEventListener(
			SimulationTimeEventListener timeEventListener) {
		this.timeEventListener = timeEventListener;
	}

	// Method used by the simulation

	public void packetReceived(byte[] packet, double rssi) {
		packetReceiver.packetReceived(packet, rssi);
	}

	public void at(double absoluteTime) {
		timeEventListener.at(timeDoubleToLong(absoluteTime));
	}

	// The method used by publisher
	public void sendPacket(byte[] packet, String recipient) {
		networkProvider.sendPacket(id, packet, recipient);
	}

	public double getPositionX() {
		return positionProvider.getPositionX(this);
	}

	public double getPositionY() {
		return positionProvider.getPositionY(this);
	}

	@Override
	public long getCurrentTime() {
		return timeProvider.getCurrentTime();
	}
	
	public void finalize() {
		packetReceiver.clearCachedMessages();
	}
}
