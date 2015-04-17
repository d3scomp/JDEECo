package cz.cuni.mff.d3s.jdeeco.network.l2.strategy;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Strategy;
import cz.cuni.mff.d3s.jdeeco.network.l2.Layer2;

/**
 * Network Layer 2 rebroadcast strategy
 * 
 * Using simple stochastic TTL
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class RebroadcastStrategy implements DEECoPlugin, L2Strategy {
	public static final double DROP_RATE = 0.5;
	
	private Layer2 layer2;
	private Random random;
	
	@Override
	public void processL2Packet(L2Packet packet) {
		// Stochastic TTL
		if(random.nextDouble() < DROP_RATE)
			return;
		
		layer2.sendL2Packet(packet, MANETBroadcastAddress.BROADCAST);
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		random = new Random(container.getId());

		// Get layer2
		Network network = container.getPluginInstance(Network.class); 
		layer2 = network.getL2();
		
		// Register this as L2 strategy
		layer2.registerL2Strategy(this);
	}
}
