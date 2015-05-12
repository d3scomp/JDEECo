/**
 * Integration test for L2 rebroadcast with communication boundary
 * 
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */

package cz.cuni.mff.d3s.jdeeco.network.l2.strategy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.SimpleBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.RebroadcastStrategy;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

public class L2RebroadcastWithBoundaryIntegrationTest {
	@Test
	public void testBoundedRebroadcast() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream outputStream = new PrintStream(baos);
		
		// Create main application container
		SimulationTimer simulationTimer = new DiscreteEventTimer();
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new SimpleBroadcastDevice(100, 10, SimpleBroadcastDevice.DEFAULT_RANGE));
		realm.addPlugin(Network.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		realm.addPlugin(RebroadcastStrategy.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		
		// Create DEECo node 1 - leader
		DEECoNode deeco0 = realm.createNode(new PositionPlugin(0, 0));
		deeco0.deployComponent(new DataSource("Src0"));
		deeco0.deployEnsemble(DataExchange.class);
	
		// Create DEECo node 2 - follower (in range of leader)
		DEECoNode deeco1 = realm.createNode(new PositionPlugin(0, SimpleBroadcastDevice.DEFAULT_RANGE * 2/3));
		deeco1.deployComponent(new DataSink("Sink0", outputStream));
		deeco1.deployEnsemble(DataExchange.class);
		
		// Create DEECo node 3 - follower (out of range of leader)
		DEECoNode deeco2 = realm.createNode(new PositionPlugin(0, SimpleBroadcastDevice.DEFAULT_RANGE * 4/3));
		deeco2.deployComponent(new DataSink("Sink1", outputStream));
		deeco2.deployEnsemble(DataExchange.class);
		
		// Create DEECo node 4 - follower (out of range of leader and Sink0, and shielded by boundary condition)
		DEECoNode deeco3 = realm.createNode(new PositionPlugin(0, SimpleBroadcastDevice.DEFAULT_RANGE * 6/3));
		deeco3.deployComponent(new DataSink("Sink2", outputStream));
		deeco3.deployEnsemble(DataExchange.class);
		
	
		// WHEN simulation is performed
		realm.start(15000);
	
		// Check output for expected values. Sink 2 should be not covered by rebroadcasts
		org.junit.Assert.assertTrue(baos.toString().contains("Sink0: 42"));
		org.junit.Assert.assertTrue(baos.toString().contains("Sink1: 42"));
		org.junit.Assert.assertTrue(baos.toString().contains("Sink2: 0"));
	}
}
