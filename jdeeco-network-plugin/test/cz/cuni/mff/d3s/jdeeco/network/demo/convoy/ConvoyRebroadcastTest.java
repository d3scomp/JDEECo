package cz.cuni.mff.d3s.jdeeco.network.demo.convoy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.demo.convoy.ConvoyEnsemble;
import cz.cuni.mff.d3s.deeco.demo.convoy.Follower;
import cz.cuni.mff.d3s.deeco.demo.convoy.Leader;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogWritersMock;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.SimpleBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.RebroadcastStrategy;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class ConvoyRebroadcastTest {

	RuntimeLogWritersMock runtimeLogWriters;
	
	@Before
	public void setUp() throws IOException{
		runtimeLogWriters = new RuntimeLogWritersMock();
	}
	
	public static void main(String[] args) throws Exception {
		ConvoyRebroadcastTest test = new ConvoyRebroadcastTest();

		test.convoyRebroadcastLoopbackRunner(false);
	}

	/**
	 * Test network with broadcasting loop- back device
	 * 
	 * This tries to communicate within the broadcast device range => communication should work
	 * 
	 */
	@Test
	public void convoyRebroadcastLoopbackTest() throws Exception {
		convoyRebroadcastLoopbackRunner(true);
	}

	private void convoyRebroadcastLoopbackRunner(boolean silent) throws Exception {
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
		realm.addPlugin(new SimpleBroadcastDevice());
		realm.addPlugin(Network.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		realm.addPlugin(RebroadcastStrategy.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);

		// Create DEECo node 1 - leader
		DEECoNode deeco1 = realm.createNode(runtimeLogWriters, new PositionPlugin(0, 0));
		// Deploy components and ensembles
		deeco1.deployComponent(new Leader(outputStream,simulationTimer));
		deeco1.deployEnsemble(ConvoyEnsemble.class);

		// Create DEECo node 2 - follower (in range of leader)
		DEECoNode deeco2 = realm.createNode(runtimeLogWriters, new PositionPlugin(0, SimpleBroadcastDevice.DEFAULT_RANGE * 2 / 3));
		// Deploy components and ensembles
		deeco2.deployComponent(new Follower("F0", outputStream, simulationTimer));
		deeco2.deployEnsemble(ConvoyEnsemble.class);

		// Create DEECo node 3 - follower (out of range of leader)
		DEECoNode deeco3 = realm.createNode(runtimeLogWriters, new PositionPlugin(0, SimpleBroadcastDevice.DEFAULT_RANGE * 4 / 3));
		// Deploy components and ensembles
		deeco3.deployComponent(new Follower("F1", outputStream, simulationTimer));
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
