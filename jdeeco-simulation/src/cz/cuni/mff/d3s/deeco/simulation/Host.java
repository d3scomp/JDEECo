package cz.cuni.mff.d3s.deeco.simulation;

import cz.cuni.mff.d3s.deeco.publisher.PacketReceiver;
import static cz.cuni.mff.d3s.deeco.simulation.Simulation.timeDoubleToLong;
import cz.cuni.mff.d3s.deeco.publisher.PacketSender;

/**
 * 
 * Class representing a host in the simulation.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class Host extends PacketSender {

	private SimulationTimeEventListener timeEventListener = null;

	private final PacketReceiver packetReceiver;
	private final Simulation simulation;
	private final String id;
	
	protected Host(Simulation simulation, String id, int packetSize,
			PacketReceiver packetReceiver) {
		super(packetSize);
		this.simulation = simulation;
		this.id = id;
		this.packetReceiver = packetReceiver;
		simulation.register(this, id);
	}

	public String getId() {
		return id;
	}

	public void setSimulationTimeEventListener(
			SimulationTimeEventListener timeEventListener) {
		this.timeEventListener = timeEventListener;
	}

	public long getSimulationTime() {
		return simulation.getSimulationTime();
	}

	public void callAt(long absoluteTime) {
		simulation.callAt(absoluteTime, id);
	}

	// Method used by the simulation

	public void packetReceived(byte[] packet) {
		packetReceiver.packetReceived(packet);
	}

	public void at(double absoluteTime) {
		timeEventListener.at(timeDoubleToLong(absoluteTime));
	}

	// The method used by publisher

	@Override
	protected void sendPacket(byte[] packet, String recipient) {
		simulation.sendPacket(id, packet, recipient);
	}
}
