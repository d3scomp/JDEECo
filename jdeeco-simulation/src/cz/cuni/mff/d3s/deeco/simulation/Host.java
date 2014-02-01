package cz.cuni.mff.d3s.deeco.simulation;

import java.util.Arrays;

import cz.cuni.mff.d3s.deeco.publish.PacketReceiver;
import cz.cuni.mff.d3s.deeco.publish.PacketSender;
import static cz.cuni.mff.d3s.deeco.simulation.Simulation.timeDoubleToLong;

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
	
	protected Host(Simulation simulation, String id, int packetSize) {
		super(packetSize);
		this.simulation = simulation;
		this.id = id;
		this.packetReceiver = new PacketReceiver(packetSize);
		simulation.register(this, id);
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

	public long getSimulationTime() {
		return simulation.getSimulationTime();
	}

	public void callAt(long absoluteTime) {
		simulation.callAt(absoluteTime, id);
	}

	// Method used by the simulation

	public void packetReceived(byte[] packet) {
		byte [] copyPackety = Arrays.copyOf(packet, packet.length);
		packetReceiver.packetReceived(copyPackety);
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
}
