package cz.cuni.mff.d3s.jdeeco.network.device;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.IPAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.DefaultDataIDSource;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;

/**
 * Tests infrastructure loop-back networking 
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class InfrastructureLoopbackTest {
	// Nodes used in testing scenario
	DEECoContainer node0;
	DEECoContainer node1;

	// Testing packet
	byte[] TEST_DATA = {'t', 'e', 's', 't'};
	L1Packet TEST_PACKET = new L1Packet(TEST_DATA, (byte)0, 0, 0, TEST_DATA.length);
	
	/**
	 * Configures node 0 - source node
	 * 
	 * Has working layer1, used to send packets
	 */
	@Before
	public void setupNode0() {
		node0 = Mockito.mock(DEECoContainer.class);
		
		// Configure id for mocked container
		Mockito.when(node0.getId()).thenReturn(0);
		
		// Configure Network for mocked container
		Layer1 layer1 = new Layer1((byte)0, DefaultDataIDSource.getInstance());
		Network network = Mockito.mock(Network.class);
		Mockito.when(network.getL1()).thenReturn(layer1);
		Mockito.when(node0.getPluginInstance(Network.class)).thenReturn(network);
	}
	
	/**
	 * Configures node 1 - destination node
	 * 
	 * Has mocked layer1, used to confirm packet reception
	 */
	@Before
	public void setupNode1() {
		node1 = Mockito.mock(DEECoContainer.class);
		
		// Configure id for mocked container
		Mockito.when(node1.getId()).thenReturn(1);
		
		// Configure Network for mocked container
		Layer1 layer1 = Mockito.mock(Layer1.class);
		Network network = Mockito.mock(Network.class);
		Mockito.when(network.getL1()).thenReturn(layer1);
		Mockito.when(node1.getPluginInstance(Network.class)).thenReturn(network);
	}
		
	/**
	 * Tests sending packet from one node to another one
	 */
	@Test
	public void testNodeToNodeRouting() {
		InfrastructureLoopback loop = new InfrastructureLoopback();
		
		// Register nodes 0 and 1 with Infrastructure loop-back
		loop.init(node0);
		loop.init(node1);
		
		// Send packet from node 0 to node 1
		Layer1 node0layer1 = node0.getPluginInstance(Network.class).getL1();
		node0layer1.sendL1Packet(TEST_PACKET, new IPAddress(String.valueOf(node1.getId())));
		
		// Test packet was received on node 1
		Layer1 node1layer1 = node1.getPluginInstance(Network.class).getL1();
		Mockito.verify(node1layer1, Mockito.atLeastOnce()).processL0Packet(Mockito.any(), Mockito.any(), Mockito.any());
	}
	
	/**
	 * Tests sending packet from node to nonexistent node
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testNodeToVoidRouting() {
		InfrastructureLoopback loop = new InfrastructureLoopback();
		
		// Register nodes 0 with Infrastructure loop-back
		loop.init(node0);
		
		// Send packet from node 0 to node 5 (non existent)
		Layer1 node0layer1 = node0.getPluginInstance(Network.class).getL1();
		node0layer1.sendL1Packet(TEST_PACKET, new IPAddress(String.valueOf(5)));
		
		// Exception should be thrown as destination node is not present
	}
}
