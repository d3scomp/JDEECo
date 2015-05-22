package demo.broadcast;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.jdeeco.core.demo.convoy.ConvoyEnsemble;
import cz.cuni.mff.d3s.jdeeco.core.demo.convoy.Follower;
import cz.cuni.mff.d3s.jdeeco.core.demo.convoy.Leader;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.RebroadcastStrategy;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTSimulation;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * Convoy demo with broadcast networking provided by OMNeT
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class BroadcastConvoyPerformanceTest {
	final static long SIMULATION_DURATION_MS = 60000;
	final static int PLAYGROUND_WIDTH = 1000;
	final static int PLAYGROUND_HEIGHT = 1000;
	final static int NODES = 50;
	
	public static void main(String[] args) throws Exception {
		OMNeTSimulation omnet = new OMNeTSimulation();

		// Create main application container
		DEECoSimulation simulation = new DEECoSimulation(omnet.getTimer());
		simulation.addPlugin(Network.class);
		simulation.addPlugin(KnowledgeInsertingStrategy.class);
		simulation.addPlugin(DefaultKnowledgePublisher.class);
		simulation.addPlugin(RebroadcastStrategy.class);
		simulation.addPlugin(omnet);
		
		
		final Random rand = new Random(42);
		boolean first = true;

		for (int i = 0; i < NODES; ++i) {
			// Create DEECo node 1 - leader
			DEECoNode deeco = simulation.createNode(i,
					new OMNeTBroadcastDevice(),
					new PositionPlugin(
							rand.nextInt(PLAYGROUND_WIDTH),
							rand.nextInt(PLAYGROUND_HEIGHT)
					)
			);

			if (rand.nextBoolean() && first) {
				first = false;
				// Deploy components and ensembles
				deeco.deployComponent(new Leader("L" + i, System.out, omnet.getTimer()));
			} else {
				// Deploy components and ensembles
				deeco.deployComponent(new Follower("F" + i, System.out, omnet.getTimer()));
			}

			deeco.deployEnsemble(ConvoyEnsemble.class);
		}
		
		long startTime = System.currentTimeMillis();

		// Run the simulation
		simulation.start(SIMULATION_DURATION_MS);
		
		long endTime = System.currentTimeMillis();
		
		System.out.format("Done, %d ms simulated in %d real ms. Sim/Real %f%n", SIMULATION_DURATION_MS, endTime - startTime, (double)(SIMULATION_DURATION_MS) / (endTime - startTime));
	}
}
