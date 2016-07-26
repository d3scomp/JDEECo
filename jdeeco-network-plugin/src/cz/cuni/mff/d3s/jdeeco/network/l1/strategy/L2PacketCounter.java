package cz.cuni.mff.d3s.jdeeco.network.l1.strategy;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginInitFailedException;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.l1.L2PacketProcessor;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Strategy;

public class L2PacketCounter implements DEECoPlugin {
	class SendProbe implements L2PacketProcessor {
		@Override
		public boolean processL2Packet(L2Packet l2Packet, Address address) {
			sendCounter++;
			return true;
		}
	}
	
	class ReceiveProbe implements L2Strategy {
		@Override
		public void processL2Packet(L2Packet packet) {
			receivedCounter++;			
		}
	}
	
	public long sendCounter = 0;
	public long receivedCounter = 0;
	
	private SendProbe sendProbe = new SendProbe();
	private ReceiveProbe receiveProbe = new ReceiveProbe();
	
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}
	
	@Override
	public void init(DEECoContainer container) throws PluginInitFailedException {
		Network network = container.getPluginInstance(Network.class);
		network.getL2().addL2PacketProcessor(sendProbe);
		network.getL2().registerL2Strategy(receiveProbe);
	}
	
	@Override
	public String toString() {
		return String.format("sendL2: %d, receivedL2: %d", sendCounter, receivedCounter);
	}
}
