package cz.cuni.mff.d3s.jdeeco.network.l1.strategy;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Strategy;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;

/**
 * Simple low-level rebroadcast
 * 
 * This rebroadcasts packets received from wireless MANET device.
 * 
 * This does not use RSSI to calculate rebroadcast time, rebroadcast is performed immediately.
 * 
 * Stochastic TTL is used to avoid congestion.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class LowLevelRebroadcastStrategy implements DEECoPlugin, L1Strategy {
	public static double DROP_RATE = 0.35;
	
	private Layer1 layer1;
	private Random random;

	@Override
	public void processL1Packet(L1Packet packet) {
		// Rebroadcast packet only if received from MANET
		if (!(packet.receivedInfo.srcAddress instanceof MANETBroadcastAddress))
			return;
		
		// Apply random packet drop to avoid congestion
		if(random.nextDouble() < DROP_RATE)
			return;
		
		layer1.sendL1Packet(packet, MANETBroadcastAddress.BROADCAST);
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		// Initialize random engine
		random = new Random(container.getId());
		
		// Resolve layer1 network
		Network network = container.getPluginInstance(Network.class);
		layer1 = network.getL1();

		// Register this as layer 1 strategy
		layer1.registerL1Strategy(this);
	}
}
