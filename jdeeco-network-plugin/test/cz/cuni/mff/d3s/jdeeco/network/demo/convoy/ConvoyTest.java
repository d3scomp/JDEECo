package cz.cuni.mff.d3s.jdeeco.network.demo.convoy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.demo.convoy.ConvoyEnsemble;
import cz.cuni.mff.d3s.deeco.demo.convoy.Follower;
import cz.cuni.mff.d3s.deeco.demo.convoy.Leader;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogWritersMock;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.SimpleBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class ConvoyTest {

	RuntimeLogWritersMock runtimeLogWriters;
	
	@Before
	public void setUp() throws IOException{
		runtimeLogWriters = new RuntimeLogWritersMock();
	}
	
	public static void main(String[] args) throws Exception {
		ConvoyTest test = new ConvoyTest();

		test.convoyLoopbackRunner(false);
	}

	/**
	 * Test network with broadcasting loop-back device
	 * 
	 * This tries to communicate within the broadcast device range => communication should work
	 * 
	 * @throws AnnotationProcessorException
	 * @throws InterruptedException
	 * @throws DEECoException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test
	public void convoyLoopbackTest() throws Exception {
		convoyLoopbackRunner(true);
	}
	
	public void convoyLoopbackRunner(boolean silent) throws Exception {
		// In silent mode the output is kept in ByteArrayOutputStream and then tested
		// whether it's correct. In non-silent mode the output is not tested, but printed to console.
		PrintStream outputStream;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (silent) {
			outputStream = new PrintStream(baos);
		} else {
			outputStream = System.out;
		}
		
		/* create main application container */
		SimulationTimer simulationTimer = new DiscreteEventTimer(); // also "new WallTimeSchedulerNotifier()"
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new SimpleBroadcastDevice());
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		
		/* create first deeco node */
		DEECoNode deeco1 = realm.createNode(runtimeLogWriters);
		/* deploy components and ensembles */
		deeco1.deployComponent(new Leader(outputStream, simulationTimer));
		deeco1.deployEnsemble(ConvoyEnsemble.class);

		/* create second deeco node */
		DEECoNode deeco2 = realm.createNode(runtimeLogWriters);
		/* deploy components and ensembles */
		deeco2.deployComponent(new Follower(outputStream, simulationTimer));
		deeco2.deployEnsemble(ConvoyEnsemble.class);

		/* WHEN simulation is performed */
		realm.start(20000);

		// THEN the follower prints out the following (ass the networking should work)
		assertThat(baos.toString(), containsString("Follower F: me = (1,3) leader = (1,3)"));
	}
	
	/**
	 * Test network with broadcasting loop- back device
	 * 
	 * This tries to communicate out of the broadcast device range => communication should NOT work
	 * 
	 * @throws AnnotationProcessorException
	 * @throws InterruptedException
	 * @throws DEECoException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test
	public void convoyLoopbackOutOfrange() throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		convoyLoopbackOutOfRangeRunner(true);
	}
	
	public void convoyLoopbackOutOfRangeRunner(boolean silent) throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		// In silent mode the output is kept in ByteArrayOutputStream and then tested
		// whether it's correct. In non-silent mode the output is not tested, but printed to console.
		PrintStream outputStream;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (silent) {
			outputStream = new PrintStream(baos);
		} else {
			outputStream = System.out;
		}
		
		/* create main application container */
		SimulationTimer simulationTimer = new DiscreteEventTimer(); // also "new WallTimeSchedulerNotifier()"
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new SimpleBroadcastDevice());
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		
		/* create first deeco node */
		DEECoNode deeco1 = realm.createNode(runtimeLogWriters, new PositionPlugin(0, SimpleBroadcastDevice.DEFAULT_RANGE));
		/* deploy components and ensembles */
		deeco1.deployComponent(new Leader(outputStream, simulationTimer));
		deeco1.deployEnsemble(ConvoyEnsemble.class);

		/* create second deeco node */
		DEECoNode deeco2 = realm.createNode(runtimeLogWriters, new PositionPlugin(SimpleBroadcastDevice.DEFAULT_RANGE, 0));
		/* deploy components and ensembles */
		deeco2.deployComponent(new Follower(outputStream, simulationTimer));
		deeco2.deployEnsemble(ConvoyEnsemble.class);

		/* WHEN simulation is performed */
		realm.start(20000);

		// THEN the follower prints out the following (as the network reach is too short)
		if (silent)
			assertThat(baos.toString(), not(containsString("Follower F: me = (1,3) leader = (1,3)")));
	}
}
