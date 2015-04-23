package cz.cuni.mff.d3s.jdeeco.network.demo.convoy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
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
import cz.cuni.mff.d3s.jdeeco.network.l1.strategy.LowLevelRebroadcastStrategy;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class ConvoyLowLevelRebroadcastTest {
	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		ConvoyLowLevelRebroadcastTest test = new ConvoyLowLevelRebroadcastTest();

		test.testConvoyLowLevelRebroadcastLoopback();
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
	@Test @Ignore("Nondeterministaly fails, Layer 1 rebroadcasting is not realiabale nor efficient")
	public void testConvoyLowLevelRebroadcastLoopback() throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		final int PUBLISH_DELAY = 50;
		
		// Create main application container
		SimulationTimer simulationTimer = new DiscreteEventTimer(); // also "new WallTimeSchedulerNotifier()"
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new BroadcastLoopback(100, 10, 250));
		realm.addPlugin(Network.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		realm.addPlugin(LowLevelRebroadcastStrategy.class);
		
		// Create DEECo node 1 - leader
		DEECoNode deeco1 = realm.createNode(new PositionPlugin(0, 0), new DefaultKnowledgePublisher(PUBLISH_DELAY));
		// Deploy components and ensembles
		deeco1.deployComponent(new Leader());
		deeco1.deployEnsemble(ConvoyEnsemble.class);

		// Create DEECo node 2 - follower (in range of leader)
		DEECoNode deeco2 = realm.createNode(new PositionPlugin(0, BroadcastLoopback.DEFAULT_RANGE * 2/3), new DefaultKnowledgePublisher(PUBLISH_DELAY));
		// Deploy components and ensembles
		deeco2.deployComponent(new Follower("F0"));
		deeco2.deployEnsemble(ConvoyEnsemble.class);
		
		// Create DEECo node 3 - follower (out of range of leader)
		DEECoNode deeco3 = realm.createNode(new PositionPlugin(0, BroadcastLoopback.DEFAULT_RANGE * 4/3), new DefaultKnowledgePublisher(PUBLISH_DELAY));
		// Deploy components and ensembles
		deeco3.deployComponent(new Follower("F1"));
		deeco3.deployEnsemble(ConvoyEnsemble.class);

		// WHEN simulation is performed
		realm.start(30000);

		// THEN the follower prints out the following (ass the networking should work)
		assertThat(log.getLog(), containsString("Follower F0: me = (1,3) leader = (1,3)"));
		assertThat(log.getLog(), containsString("Follower F1: me = (1,3) leader = (1,3)"));
	}
}
