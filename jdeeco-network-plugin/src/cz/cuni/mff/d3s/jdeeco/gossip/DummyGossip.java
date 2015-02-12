package cz.cuni.mff.d3s.jdeeco.gossip;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.jdeeco.network.Network;

/**
 * Trivial gossip layer implementation, just passes data to network
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class DummyGossip implements Gossip {
	private Network network;
	private Scheduler scheduler;

	private static int PERIOD = 1000;

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		// Resolve network layer dependencies
		network = container.getPluginInstance(Network.class);
		scheduler = container.getRuntimeFramework().getScheduler();

		// TODO: Register periodic update task
	}

	@Override
	public void processKnowledgeUpdate(Object knowledge) {

	}
}
