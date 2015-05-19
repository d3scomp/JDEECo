package cz.cuni.mff.d3s.jdeeco.network.demo.convoy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.SimpleBroadcastDevice;
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
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException {
		ConvoyLowLevelRebroadcastTest test = new ConvoyLowLevelRebroadcastTest();

		test.convoyLowLevelRebroadcastLoopbackRunner(false);
	}

	/**
	 * Test network with broadcasting loop- back device
	 * 
	 * This tries to communicate within the broadcast device range => communication should work
	 * 
	 * @throws Exception
	 */
	@Test
	public void convoyLowLevelRebroadcastLoopbackTest() throws Exception {
		convoyLowLevelRebroadcastLoopbackRunner(true);
	}

	private void convoyLowLevelRebroadcastLoopbackRunner(boolean silent) throws AnnotationProcessorException,
			InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		// In silent mode the output is kept in ByteArrayOutputStream and then tested
		// whether it's correct. In non-silent mode the output is not tested, but printed to console.
		PrintStream outputStream;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (silent) {
			outputStream = new PrintStream(baos);
		} else {
			outputStream = System.out;
		}

		// Create main application container
		SimulationTimer simulationTimer = new DiscreteEventTimer(); // also "new WallTimeSchedulerNotifier()"
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new SimpleBroadcastDevice(100, 10, SimpleBroadcastDevice.DEFAULT_RANGE,
				SimpleBroadcastDevice.DEFAULT_MTU));
		realm.addPlugin(Network.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		realm.addPlugin(LowLevelRebroadcastStrategy.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);

		// Create DEECo node 1 - leader
		DEECoNode deeco1 = realm.createNode(new PositionPlugin(0, 0));
		// Deploy components and ensembles
		deeco1.deployComponent(new Leader(outputStream));
		deeco1.deployEnsemble(ConvoyEnsemble.class);

		// Create DEECo node 2 - follower (in range of leader)
		DEECoNode deeco2 = realm.createNode(new PositionPlugin(0, SimpleBroadcastDevice.DEFAULT_RANGE * 2 / 3));
		// Deploy components and ensembles
		deeco2.deployComponent(new Follower("F0", outputStream));
		deeco2.deployEnsemble(ConvoyEnsemble.class);

		// Create DEECo node 3 - follower (out of range of leader)
		DEECoNode deeco3 = realm.createNode(new PositionPlugin(0, SimpleBroadcastDevice.DEFAULT_RANGE * 4 / 3));
		// Deploy components and ensembles
		deeco3.deployComponent(new Follower("F1", outputStream));
		deeco3.deployEnsemble(ConvoyEnsemble.class);

		// WHEN simulation is performed
		realm.start(15000);

		// THEN the follower prints out the following (ass the networking should work)
		if (silent) {
			assertThat(baos.toString(), containsString("Follower F0: me = (1,3) leader = (1,3)"));
			assertThat(baos.toString(), containsString("Follower F1: me = (1,3) leader = (1,3)"));
		}
	}
}
