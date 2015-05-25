package demo.broadcast;

import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
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
public class BroadcastConvoyPerformanceGridTest {
	final static long SIMULATION_DURATION_MS = 60000;
	final static int PLAYGROUND_WIDTH = 1000;
	final static int PLAYGROUND_HEIGHT = 1000;
	final static int SPACING_X = 100;
	final static int SPACING_Y = 100;
	final static int ROWS = 10;
	final static int COLUMNS = 10;

	public static void main(String[] args) throws Exception {
		OMNeTSimulation omnet = new OMNeTSimulation();

		// Create main application container
		DEECoSimulation simulation = new DEECoSimulation(omnet.getTimer());
		simulation.addPlugin(Network.class);
		simulation.addPlugin(KnowledgeInsertingStrategy.class);
		simulation.addPlugin(DefaultKnowledgePublisher.class);
		simulation.addPlugin(RebroadcastStrategy.class);
		simulation.addPlugin(omnet);

		boolean first = true;

		for (int row = 0; row < ROWS; ++row) {
			for (int column = 0; column < COLUMNS; ++column) {
				int index = row * COLUMNS + column;
				if (index % 2 == 0)
					continue;

				double x = column * SPACING_X;
				double y = row * SPACING_Y;

				System.out.format("Placing %d at [%d, %d]%n", index, (int) (x), (int) (y));

				// Create DEECo node 1 - leader
				DEECoNode deeco = simulation.createNode(new OMNeTBroadcastDevice(), new PositionPlugin(x, y));

				if (first) {
					first = false;
					// Deploy components and ensembles
					deeco.deployComponent(new Seeder("L" + index, System.out, omnet.getTimer()));
				} else {
					// Deploy components and ensembles
					deeco.deployComponent(new Grather("F" + index, System.out, omnet.getTimer()));
				}

				deeco.deployEnsemble(DataPass.class);
			}
		}

		long startTime = System.currentTimeMillis();

		// Run the simulation
		simulation.start(SIMULATION_DURATION_MS);

		long endTime = System.currentTimeMillis();

		System.out.format("Done, %d ms simulated in %d real ms. Sim/Real %f%n", SIMULATION_DURATION_MS, endTime
				- startTime, (double) (SIMULATION_DURATION_MS) / (endTime - startTime));
	}
}
