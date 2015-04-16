package cz.cuni.mff.d3s.jdeeco.network.demo.convoy;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.BroadcastLoopback;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.position.PositionAware;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class ConvoyTest {
	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		ConvoyTest test = new ConvoyTest();

		test.testConvoyLoopback();
	}

	/**
	 * Test network with broadcasting loop- back device
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
	public void testConvoyLoopback() throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		/* create main application container */
		SimulationTimer simulationTimer = new DiscreteEventTimer(); // also "new WallTimeSchedulerNotifier()"
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new BroadcastLoopback());
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		
		/* create first deeco node */
		DEECoNode deeco1 = realm.createNode(new PositionAware(0, 0));
		/* deploy components and ensembles */
		deeco1.deployComponent(new Leader());
		deeco1.deployEnsemble(ConvoyEnsemble.class);

		/* create second deeco node */
		DEECoNode deeco2 = realm.createNode(new PositionAware(0, BroadcastLoopback.DEFAULT_RANGE / 2));
		/* deploy components and ensembles */
		deeco2.deployComponent(new Follower());
		deeco2.deployEnsemble(ConvoyEnsemble.class);

		/* WHEN simulation is performed */
		realm.start(20000);

		// THEN the follower prints out the following (ass the networking should work)
		assertThat(log.getLog(), containsString("Follower F: me = (1,3) leader = (1,3)"));
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
	public void testConvoyLoopbackOutOfrange() throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		/* create main application container */
		SimulationTimer simulationTimer = new DiscreteEventTimer(); // also "new WallTimeSchedulerNotifier()"
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new BroadcastLoopback());
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		
		/* create first deeco node */
		DEECoNode deeco1 = realm.createNode(new PositionAware(0, BroadcastLoopback.DEFAULT_RANGE));
		/* deploy components and ensembles */
		deeco1.deployComponent(new Leader());
		deeco1.deployEnsemble(ConvoyEnsemble.class);

		/* create second deeco node */
		DEECoNode deeco2 = realm.createNode(new PositionAware(BroadcastLoopback.DEFAULT_RANGE, 0));
		/* deploy components and ensembles */
		deeco2.deployComponent(new Follower());
		deeco2.deployEnsemble(ConvoyEnsemble.class);

		/* WHEN simulation is performed */
		realm.start(20000);

		// THEN the follower prints out the following (as the network reach is too short)
		assertThat(log.getLog(), not(containsString("Follower F: me = (1,3) leader = (1,3)")));
	}
}
