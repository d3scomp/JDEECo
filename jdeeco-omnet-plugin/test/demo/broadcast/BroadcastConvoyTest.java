package demo.broadcast;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTSimulation;
import cz.cuni.mff.d3s.jdeeco.network.omnet.Position;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * Convoy demo with broadcast networking provided by OMNeT
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class BroadcastConvoyTest {
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException {
		BroadcastConvoyTest test = new BroadcastConvoyTest();
		test.testConvoyOmnet();
	}

	// TODO: Can we really run OMNeT based integration tests
	// @Test
	public void testConvoyOmnet() throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException {
		OMNeTSimulation omnet = new OMNeTSimulation();

		// Create main application container
		DEECoSimulation simulation = new DEECoSimulation(omnet.getTimer());
		simulation.addPlugin(Network.class);
		simulation.addPlugin(KnowledgeInsertingStrategy.class);
		simulation.addPlugin(DefaultKnowledgePublisher.class);
		simulation.addPlugin(omnet);

		DEECoNode node0 = simulation.createNode(new OMNeTBroadcastDevice(new Position(100, 0, 0)));
		DEECoNode node1 = simulation.createNode(new OMNeTBroadcastDevice(new Position(0, 100, 0)));

		// Deploy components and ensembles
		node0.deployComponent(new Leader());
		node0.deployEnsemble(ConvoyEnsemble.class);

		node1.deployComponent(new Follower());
		node1.deployEnsemble(ConvoyEnsemble.class);

		simulation.start(60000);
	}
}
