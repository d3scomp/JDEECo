package cz.cuni.mff.d3s.jdeeco.simulation.demo;
import java.util.Arrays;

import cz.cuni.mff.d3s.deeco.publish.PacketReceiver;


public class TestPacketReceiver extends PacketReceiver {

	public TestPacketReceiver(int packetSize) {
		super(packetSize);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void packetReceived(byte[] packet) {
		System.out.println("Received: " + Arrays.toString(packet));
		super.packetReceived(packet);
	}

}
