package cz.cuni.mff.d3s.jdeeco.network.l2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import cz.cuni.mff.d3s.jdeeco.network.Address;
import cz.cuni.mff.d3s.jdeeco.network.L2PacketType;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.MarshallerRegistry;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.SerializingMarshaller;

/**
 * Tests for layer 2 jDEECo networking
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class L2Test {
	private Layer2 l2Layer;
	private MarshallerRegistry registry = new MarshallerRegistry();

	@Mock
	private Layer1 layer1;

	private final String PAYLOAD = "Dummy Payload";

	/**
	 * Checks whenever the pay-load object matches expected value
	 * 
	 * @param payload
	 */
	private void assertPayload(Object payload) {
		assertEquals(PAYLOAD, payload);
	}

	/**
	 * Initializes L2 layer
	 */
	@Before
	public void initializeL2() {
		// Setup marshallers to be used by layer
		registry.registerMarshaller(L2PacketType.KNOWLEDGE, new SerializingMarshaller());

		// Instantiate layer
		l2Layer = new Layer2(layer1, registry);
	}

	/**
	 * Tests L2 packet marshaling availability and consistency
	 */
	@Test
	public void testL2PacketMarshalling() {
		// Create source packet
		L2Packet srcPacket = l2Layer.createPacket(new PacketHeader(L2PacketType.KNOWLEDGE), PAYLOAD);
		assertPayload(srcPacket.getObject());

		// Create destination packet from source packet binary data
		L2ReceivedInfo info = new L2ReceivedInfo(new LinkedList<L1Packet>(), 1, 1);
		L2Packet dstPacket = new L2Packet(l2Layer, srcPacket.getData(), info);
		assertPayload(dstPacket.getObject());
	}

	/**
	 * Tests passing L2 packet via L2 layer to L1 layer
	 */
	@Test
	public void testL2PacketSending() {
		// Create source packet
		L2Packet srcPacket = l2Layer.createPacket(new PacketHeader(L2PacketType.KNOWLEDGE), PAYLOAD);
		assertPayload(srcPacket.getObject());

		// TODO: Address is fake
		Address address = new Address() {
		};

		l2Layer.sendL2Packet(srcPacket, address);

		// Check packet was passed to layer1
		Mockito.verify(layer1).sendL2Packet(Matchers.eq(srcPacket), Matchers.eq(address));
	}

	/**
	 * Tests passing processed L2 packet to L2 strategy
	 */
	@Test
	public void testL2PacketProcessing() {
		// Register strategy with the L2
		L2Strategy strategy = Mockito.mock(L2Strategy.class);
		l2Layer.registerL2Strategy(strategy);

		L2ReceivedInfo info = new L2ReceivedInfo(new LinkedList<L1Packet>(), 1, 1);

		// Create source packet (created with data and received packet info)
		byte[] data = registry.marshall(L2PacketType.KNOWLEDGE, PAYLOAD);
		L2Packet srcPacket = new L2Packet(l2Layer, L2Packet.createL2PacketData(new PacketHeader(L2PacketType.KNOWLEDGE),
				data), info);
		assertPayload(srcPacket.getObject());

		// Process packet
		l2Layer.processL2Packet(srcPacket);

		// Check packet was processed by strategy
		Mockito.verify(strategy).processL2Packet(Matchers.eq(srcPacket));
	}

	/**
	 * Test registering/unregistering L2 strategy
	 */
	@Test
	public void testL2StrategyManagement() {
		// Create mock strategy
		L2Strategy strategy = Mockito.mock(L2Strategy.class);

		// Register strategy
		l2Layer.registerL2Strategy(strategy);

		// Check strategy is registered
		assertTrue(l2Layer.getRegisteredL2Strategies().contains(strategy));

		// Unregister strategy
		l2Layer.unregisterL2Strategy(strategy);

		// Check strategy is unregistered
		assertFalse(l2Layer.getRegisteredL2Strategies().contains(strategy));
	}

	/**
	 * Test forbidden L2 strategy modification
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testL2StrategyListReadOnly() {
		// Create mock strategy
		L2Strategy strategy = Mockito.mock(L2Strategy.class);

		// Try to modify returned collection
		l2Layer.getRegisteredL2Strategies().add(strategy);
	}
}
