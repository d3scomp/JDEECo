package cz.cuni.mff.d3s.deeco.simulation;

import static cz.cuni.mff.d3s.deeco.simulation.Simulation.timeDoubleToLong;
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
public class Host extends PacketSender implements CurrentTimeProvider {
	

	private SimulationTimeEventListener timeEventListener = null;

	private final PacketReceiver packetReceiver;
	private final Simulation simulation;
	private final String id;
	private final boolean hasMANETNic;
	private final boolean hasIPNic;
	
	protected Host(Simulation simulation, String jDEECoAppModuleId, boolean hasMANETNic, boolean hasIPNic) {
		super(jDEECoAppModuleId);
		this.simulation = simulation;
		this.id = jDEECoAppModuleId;
		this.packetReceiver = new PacketReceiver(id);
		this.packetReceiver.setCurrentTimeProvider(this);
		this.hasMANETNic = hasMANETNic;
		this.hasIPNic = hasIPNic;
		simulation.register(this, id);
	}
	
	public Host(Simulation simulation, String jDEECoAppModuleId) {
		this(simulation, jDEECoAppModuleId, true, true);
	}
	
	public boolean hasMANETNic() {
		return hasMANETNic;
	}
	
	public boolean hasIPNic() {
		return hasIPNic;
	}

	public PacketReceiver getPacketReceiver() {
		return packetReceiver;
	}

	public String getId() {
		return id;
	}

	public void setSimulationTimeEventListener(
			SimulationTimeEventListener timeEventListener) {
		this.timeEventListener = timeEventListener;
	}

	public void callAt(long absoluteTime) {
		simulation.callAt(absoluteTime, id);
	}

	// Method used by the simulation

	public void packetReceived(byte[] packet, double rssi) {
		packetReceiver.packetReceived(packet, rssi);
	}

	public void at(double absoluteTime) {
		timeEventListener.at(timeDoubleToLong(absoluteTime));
	}

	// The method used by publisher

	@Override
	protected void sendPacket(byte[] packet, String recipient) {
		simulation.sendPacket(id, packet, recipient);
	}

	public double getPositionX() {
		return simulation.getPositionX(id);
	}

	public double getPositionY() {
		return simulation.getPositionY(id);
	}

	@Override
	public long getCurrentTime() {
		return simulation.getSimulationTime();
	}
}
