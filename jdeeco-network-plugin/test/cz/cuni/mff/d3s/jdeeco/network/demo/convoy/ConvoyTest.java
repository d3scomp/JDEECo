package cz.cuni.mff.d3s.jdeeco.network.demo.convoy;

import static org.hamcrest.CoreMatchers.containsString;
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
import cz.cuni.mff.d3s.jdeeco.network.device.LoopbackBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.publishing.DummyKnowledgePublisher;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class ConvoyTest {
	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException {
		ConvoyTest test = new ConvoyTest();

		test.testConvoyLoopback();
	}

	/**
	 * Test network with broadcasting loop- back device
	 * 
	 * @throws AnnotationProcessorException
	 * @throws InterruptedException
	 * @throws DEECoException
	 */
	@Test
	public void testConvoyLoopback() throws AnnotationProcessorException, InterruptedException, DEECoException {
		/* create main application container */
		SimulationTimer simulationTimer = new DiscreteEventTimer(); // also "new WallTimeSchedulerNotifier()"
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);

		LoopbackBroadcastDevice loopback = new LoopbackBroadcastDevice();

		/* create first deeco node */
		DEECoNode deeco1 = realm.createNode(new Network(), new DummyKnowledgePublisher(),
				new KnowledgeInsertingStrategy(), loopback);
		/* deploy components and ensembles */
		deeco1.deployComponent(new Leader());
		deeco1.deployEnsemble(ConvoyEnsemble.class);

		/* create second deeco node */
		DEECoNode deeco2 = realm.createNode(new Network(), new DummyKnowledgePublisher(),
				new KnowledgeInsertingStrategy(), loopback);
		/* deploy components and ensembles */
		deeco2.deployComponent(new Follower());
		deeco2.deployEnsemble(ConvoyEnsemble.class);

		/* WHEN simulation is performed */
		realm.start(20000);

		// THEN the follower prints out the following (as there is no network and the components cannot exchange data)
		assertThat(log.getLog(), containsString("Follower F: me = (1,3) leader = (1,3)"));
	}
}
