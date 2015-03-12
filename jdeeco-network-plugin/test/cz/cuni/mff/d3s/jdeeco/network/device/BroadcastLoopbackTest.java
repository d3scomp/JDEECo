package cz.cuni.mff.d3s.jdeeco.network.device;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.DefaultDataIDSource;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;

/**
 * Tests broadcast loop-back networking
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class BroadcastLoopbackTest {
	// Nodes used in testing scenario
	DEECoContainer node0;
	DEECoContainer node1;
	DEECoContainer node2;

	// Mock runtime
	RuntimeFramework runtime;

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
		Scheduler scheduler = new InstantSchedulerMock();

		// Configure runtime
		runtime = Mockito.mock(RuntimeFramework.class);
		Mockito.when(runtime.getScheduler()).thenReturn(scheduler);

		// Configure source node
		node0 = setupSourceNode();

		// Configures all destination nodes
		node1 = setupDestinationNode(1);
		node2 = setupDestinationNode(2);
	}

	/**
	 * Configures node source node
	 * 
	 * Has working layer1, used to send packets
	 */
	private DEECoContainer setupSourceNode() {
		DEECoContainer node = Mockito.mock(DEECoContainer.class);

		// Configure id for mocked container
		Mockito.when(node.getId()).thenReturn(0);

		// Configure runtime
		Mockito.when(node.getRuntimeFramework()).thenReturn(runtime);

		// Configure Network for mocked container
		Layer1 layer1 = new Layer1((byte) 0, DefaultDataIDSource.getInstance());
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
		DEECoContainer node = Mockito.mock(DEECoContainer.class);

		// Configure id for mocked container
		Mockito.when(node.getId()).thenReturn(id);

		// Configure runtime
		Mockito.when(node.getRuntimeFramework()).thenReturn(runtime);

		// Configure Network for mocked container
		Layer1 layer1 = Mockito.mock(Layer1.class);
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
		BroadcastLoopback loop = new BroadcastLoopback();

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
}
