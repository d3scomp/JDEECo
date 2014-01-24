import java.util.Arrays;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.publisher.KnowledgeData;
import cz.cuni.mff.d3s.deeco.publisher.PacketReceiver;
import cz.cuni.mff.d3s.deeco.simulation.Host;
import cz.cuni.mff.d3s.deeco.simulation.Simulation;


public class TestHost extends Host {
	
	public TestHost(Simulation simulation, String id, int packetSize,
			PacketReceiver packetReceiver) {
		super(simulation, id, packetSize, packetReceiver);
	}
	
	@Override
	public void packetReceived(byte[] packet) {
		System.out.println("Host " + getId() + " has received a packet. Passing for processing...");
		super.packetReceived(packet);
	}
	
	@Override
	public void at(double absoluteTime) {
		KnowledgeData kd = new KnowledgeData(getId(), new ValueSet());
		sendData(kd, "");
	}
	
	@Override
	protected void sendPacket(byte[] packet, String recipient) {
		super.sendPacket(packet, recipient);
		System.out.println("Sending: " + Arrays.toString(packet));
	}
}
