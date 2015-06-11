package cz.cuni.mff.d3s.jdeeco.network.device;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.jdeeco.network.InstantSchedulerMock;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.DefaultDataIDSource;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.RebroadcastStrategy;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;

/**
 * Tests broadcast loop-back networking
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class SimpleBroadcastDeviceTest {
	// Nodes used in testing scenario
	DEECoContainer node0;
	DEECoContainer node1;
	DEECoContainer node2;

	// Mock runtime
	RuntimeFramework runtime;
	Scheduler scheduler;

	// Testing packet data
	byte[] TEST_DATA = { 't', 'e', 's', 't' };
	/*
	 * Testing packet
	 * 
	 * Packet total length is pay-load + 1 in order to get around problems with completing l2 packets from fake data
	 */
	L1Packet TEST_PACKET = new L1Packet(TEST_DATA, (byte) 0, 0, 0, TEST_DATA.length + 1);

	/**
	 * Configures runtime
	 */
	@Before
	public void setupRuntime() {
		// Create mock scheduler
		scheduler = new InstantSchedulerMock();

		// Configure runtime
		runtime = Mockito.mock(RuntimeFramework.class);
		Mockito.when(runtime.getScheduler()).thenReturn(scheduler);

		// Configure source node
		node0 = setupSourceNode();

		// Configures all destination nodes
		node1 = setupDestinationNode(1);
		node2 = setupDestinationNode(2);
	}

	private DEECoContainer setupBaseNode(double x, double y) {
		DEECoContainer node = Mockito.mock(DEECoContainer.class);

		// Configure id for mocked container
		Mockito.when(node.getId()).thenReturn(0);

		// Configure runtime
		Mockito.when(node.getRuntimeFramework()).thenReturn(runtime);

		// Configure position provider plugin
		Mockito.when(node.getPluginInstance(PositionPlugin.class)).thenReturn(new PositionPlugin(x, y));

		return node;
	}

	/**
	 * Configures node source node
	 * 
	 * Has working layer1, used to send packets
	 */
	private DEECoContainer setupSourceNode() {
		DEECoContainer node = setupBaseNode(0, SimpleBroadcastDevice.DEFAULT_RANGE / 2);

		// Configure Network for mocked container
		Layer1 layer1 = new Layer1((byte) 0, DefaultDataIDSource.getInstance(), scheduler);
		Network network = Mockito.mock(Network.class);
		Mockito.when(network.getL1()).thenReturn(layer1);
		Mockito.when(node.getPluginInstance(Network.class)).thenReturn(network);

		return node;
	}

	/**
	 * Configures destination node
	 * 
	 * Has mocked layer1, used to confirm packet reception
	 */
	private DEECoContainer setupDestinationNode(int id) {
		DEECoContainer node = setupBaseNode(0, 0);

		// Configure Network for mocked container
		Layer1 layer1 = Mockito.spy(new Layer1((byte) 0, DefaultDataIDSource.getInstance(), scheduler));
		//Layer1 layer1 = Mockito.mock(Layer1.class);
		Network network = Mockito.mock(Network.class);
		Mockito.when(network.getL1()).thenReturn(layer1);
		Mockito.when(node.getPluginInstance(Network.class)).thenReturn(network);

		return node;
	}

	/**
	 * Tests sending packet from one node to all other nodes
	 */
	@Test
	public void testNodeToNodeRouting() {
		SimpleBroadcastDevice loop = new SimpleBroadcastDevice();

		// Register nodes 0 and 1 with Broadcast loop-back
		loop.init(node0);
		loop.init(node1);
		loop.init(node2);

		// Send packet from node 0 to node 1
		Layer1 node0layer1 = node0.getPluginInstance(Network.class).getL1();
		node0layer1.sendL1Packet(TEST_PACKET, MANETBroadcastAddress.BROADCAST);

		// Test packet was received on node 1
		Layer1 node1layer1 = node1.getPluginInstance(Network.class).getL1();
		Mockito.verify(node1layer1, Mockito.atLeastOnce()).processL0Packet(Mockito.any(), Mockito.any(), Mockito.any());

		// Test packet was received on node 2
		Layer1 node2layer1 = node2.getPluginInstance(Network.class).getL1();
		Mockito.verify(node2layer1, Mockito.atLeastOnce()).processL0Packet(Mockito.any(), Mockito.any(), Mockito.any());
	}
	
	/**
	 * Tests RSSI approximation
	 * 
	 * Test calculation against constants and allows 10% inaccuracy
	 */
	@Test
	public void testRSSIApprox() {
		Assert.assertEquals(RebroadcastStrategy.RSSI_10m, SimpleBroadcastDevice.calcRssiForDistance(10), RebroadcastStrategy.RSSI_10m / 10);
		Assert.assertEquals(RebroadcastStrategy.RSSI_20m, SimpleBroadcastDevice.calcRssiForDistance(20), RebroadcastStrategy.RSSI_20m / 10);
		Assert.assertEquals(RebroadcastStrategy.RSSI_50m, SimpleBroadcastDevice.calcRssiForDistance(50), RebroadcastStrategy.RSSI_50m / 10);
		Assert.assertEquals(RebroadcastStrategy.RSSI_100m, SimpleBroadcastDevice.calcRssiForDistance(100), RebroadcastStrategy.RSSI_100m / 10);
		Assert.assertEquals(RebroadcastStrategy.RSSI_200m, SimpleBroadcastDevice.calcRssiForDistance(200), RebroadcastStrategy.RSSI_200m / 10);
		Assert.assertEquals(RebroadcastStrategy.RSSI_250m, SimpleBroadcastDevice.calcRssiForDistance(250), RebroadcastStrategy.RSSI_250m / 10);
	}
}
