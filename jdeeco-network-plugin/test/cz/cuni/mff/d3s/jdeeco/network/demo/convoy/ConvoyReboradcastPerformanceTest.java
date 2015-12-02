package cz.cuni.mff.d3s.jdeeco.network.demo.convoy;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.demo.convoy.ConvoyEnsemble;
import cz.cuni.mff.d3s.deeco.demo.convoy.Follower;
import cz.cuni.mff.d3s.deeco.demo.convoy.Leader;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.SimpleBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.RebroadcastStrategy;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

public class ConvoyReboradcastPerformanceTest {
	/**
	 * Tests the performance of the rebroadcast system
	 * 
	 * ### This is not to be run automatically. ###
	 * 
	 * Instantiates a lot of nodes and tries rebroadcast.
	 * 
	 * @throws Exception
	 * 
	 */
	public static void main(String[] args) throws Exception {
		final long SIMULATION_DURATION_MS = 120000;
		
		// Create main application container
		SimulationTimer simulationTimer = new DiscreteEventTimer(); // also "new WallTimeSchedulerNotifier()"
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new SimpleBroadcastDevice(25, 5, 250, 128));
		realm.addPlugin(Network.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		realm.addPlugin(RebroadcastStrategy.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);

		final int PLAYGROUND_WIDTH = 1000;
		final int PLAYGROUND_HEIGHT = 1000;
		final int NODES = 50;
		final Random rand = new Random(42);

		for (int i = 0; i < NODES; ++i) {
			// Create DEECo node 1 - leader
			DEECoNode deeco = realm.createNode(new PositionPlugin(rand.nextInt(PLAYGROUND_WIDTH), rand
					.nextInt(PLAYGROUND_HEIGHT)));

			if (rand.nextBoolean()) {
				// Deploy components and ensembles
				deeco.deployComponent(new Leader("L" + i, System.out, simulationTimer));
			} else {
				// Deploy components and ensembles
				deeco.deployComponent(new Follower("F" + i, System.out, simulationTimer));
			}

			deeco.deployEnsemble(ConvoyEnsemble.class);
		}
		
		long startTime = System.currentTimeMillis();

		// Run the simulation
		realm.start(SIMULATION_DURATION_MS);
		
		long endTime = System.currentTimeMillis();
		
		System.out.format("Done, %d ms simulated in %d real ms. Sim/Real %f%n", SIMULATION_DURATION_MS, endTime - startTime, (double)(SIMULATION_DURATION_MS) / (endTime - startTime));
	}
}
