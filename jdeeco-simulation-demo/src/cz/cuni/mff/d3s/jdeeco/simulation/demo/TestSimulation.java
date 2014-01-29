package cz.cuni.mff.d3s.jdeeco.simulation.demo;
import cz.cuni.mff.d3s.deeco.publish.PacketReceiver;
import cz.cuni.mff.d3s.deeco.simulation.Host;
import cz.cuni.mff.d3s.deeco.simulation.Simulation;


public class TestSimulation extends Simulation {

	@Override
	public Host getHost(String id, int packetSize, PacketReceiver packetReceiver) {
		return new TestHost(this, id, packetSize, packetReceiver);
	}
	
}
