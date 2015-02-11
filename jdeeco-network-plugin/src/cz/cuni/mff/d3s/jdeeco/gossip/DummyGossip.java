package cz.cuni.mff.d3s.jdeeco.gossip;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.NetworkPlugin;

/**
 * Trivial gossip layer implementation, just passes data to network
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class DummyGossip implements Gossip {
	private NetworkPlugin network;

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		// Resolve network layer dependency
		network = container.getPluginInstance(Network.class);

		// TODO Register for knowledge updates

		// container.getRuntimeFramework()

	}

	@Override
	public void processKnowledgeUpdate(Object knowledge) {
		// TODO: We should distinguish type as this is the last place where the type is known
		network.processDataFromGossipLayer(knowledge);
	}
}
