import java.util.Arrays;

import cz.cuni.mff.d3s.deeco.publisher.IncomingKnowledgeListener;
import cz.cuni.mff.d3s.deeco.publisher.PacketReceiver;


public class TestPacketReceiver extends PacketReceiver {

	public TestPacketReceiver(int packetSize,
			IncomingKnowledgeListener incomingKnowledgeListener) {
		super(packetSize, incomingKnowledgeListener);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void packetReceived(byte[] packet) {
		System.out.println("Received: " + Arrays.toString(packet));
		super.packetReceived(packet);
	}

}
