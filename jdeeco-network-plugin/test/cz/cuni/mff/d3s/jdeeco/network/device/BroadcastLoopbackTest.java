package cz.cuni.mff.d3s.jdeeco.network.device;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
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

	// Testing packet data
	byte[] TEST_DATA = { 't', 'e', 's', 't' };
	/*
	 * Testing packet
	 * 
	 * Packet total length is pay-load + 1 in order to get around problems with completing l2 packets from fake data
	 */
	L1Packet TEST_PACKET = new L1Packet(TEST_DATA, (byte) 0, 0, 0, TEST_DATA.length + 1);

	/**
	 * Configures node 0 - source node
	 * 
	 * Has working layer1, used to send packets
	 */
	@Before
	public void setupNodeSourceNode() {
		node0 = Mockito.mock(DEECoContainer.class);

		// Configure id for mocked container
		Mockito.when(node0.getId()).thenReturn(0);

		// Configure Network for mocked container
		Layer1 layer1 = new Layer1((byte) 0, DefaultDataIDSource.getInstance());
		Network network = Mockito.mock(Network.class);
		Mockito.when(network.getL1()).thenReturn(layer1);
		Mockito.when(node0.getPluginInstance(Network.class)).thenReturn(network);
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

		// Configure Network for mocked container
		Layer1 layer1 = Mockito.mock(Layer1.class);
		Network network = Mockito.mock(Network.class);
		Mockito.when(network.getL1()).thenReturn(layer1);
		Mockito.when(node.getPluginInstance(Network.class)).thenReturn(network);

		return node;
	}

	/**
	 * Configures all destination nodes
	 */
	@Before
	public void setupDestinationNodes() {
		node1 = setupDestinationNode(1);
		node2 = setupDestinationNode(2);
	}

	/**
	 * Tests sending packet from one node to all other nodes
	 */
	@Test
	public void testNodeToNodeRouting() {
		BroadcastLoopback loop = new BroadcastLoopback();

		// Register nodes 0 and 1 with Infrastructure loop-back
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
