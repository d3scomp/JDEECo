package demo.infrastructure;

import java.util.Arrays;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.IPAddress;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTInfrastructureDevice;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTSimulation;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * Convoy demo with infrastructure networking provided by OMNeT
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class InfrastructureConvoyTest {
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException {
		InfrastructureConvoyTest test = new InfrastructureConvoyTest();

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
		simulation.addPlugin(omnet);

		// Node A
		DEECoNode node0 = simulation.createNode(
				new DefaultKnowledgePublisher(Arrays.asList(new IPAddress("10.0.0.65"))),
				new OMNeTInfrastructureDevice(new IPAddress("10.0.0.60")),
				new PositionPlugin(0, 0));
		
		node0.deployComponent(new Leader());
		node0.deployEnsemble(ConvoyEnsemble.class);

		// Node B
		DEECoNode node1 = simulation.createNode(
				new DefaultKnowledgePublisher(Arrays.asList(new IPAddress("10.0.0.60"))),
				new OMNeTInfrastructureDevice(new IPAddress("10.0.0.65")),
				new PositionPlugin(0, 0));
		node1.deployComponent(new Follower());
		node1.deployEnsemble(ConvoyEnsemble.class);

		simulation.start(60000);
	}
}
