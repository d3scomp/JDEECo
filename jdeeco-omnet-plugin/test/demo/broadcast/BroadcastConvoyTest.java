package demo.broadcast;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
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
public class BroadcastConvoyTest {
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException {
		BroadcastConvoyTest test = new BroadcastConvoyTest();
		test.convoyOmnet(false);
	}

	@Test
	public void testConvoyOmnet() throws AnnotationProcessorException, InterruptedException, DEECoException,
	InstantiationException, IllegalAccessException {
		convoyOmnet(true);
	}
	
	public void convoyOmnet(boolean silent) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException {
		// In silent mode the output is kept in ByteArrayOutputStream and then tested
		// whether it's correct. In non-silent mode the output is not tested, but printed to console.
		PrintStream outputStream;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (silent) {
			outputStream = new PrintStream(baos);
		} else {
			outputStream = System.out;
		}
		
		OMNeTSimulation omnet = new OMNeTSimulation();

		// Create main application container
		DEECoSimulation simulation = new DEECoSimulation(omnet.getTimer());
		simulation.addPlugin(Network.class);
		simulation.addPlugin(KnowledgeInsertingStrategy.class);
		simulation.addPlugin(DefaultKnowledgePublisher.class);
		simulation.addPlugin(omnet);

		DEECoNode node0 = simulation.createNode(new OMNeTBroadcastDevice(), new PositionPlugin(100, 0));
		DEECoNode node1 = simulation.createNode(new OMNeTBroadcastDevice(), new PositionPlugin(0, 100));

		// Deploy components and ensembles
		node0.deployComponent(new Leader(outputStream));
		node0.deployEnsemble(ConvoyEnsemble.class);

		node1.deployComponent(new Follower(outputStream));
		node1.deployEnsemble(ConvoyEnsemble.class);

		simulation.start(60000);
		
		// THEN the follower prints out the following (ass the networking should work)
		if(silent)
			assertThat(baos.toString(), containsString("Follower F: me = (1,3) leader = (1,3)"));
	}
}
