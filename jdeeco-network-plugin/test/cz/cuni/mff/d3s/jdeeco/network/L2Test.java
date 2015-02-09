package cz.cuni.mff.d3s.jdeeco.network;

import java.util.LinkedList;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import cz.cuni.mff.d3s.jdeeco.network.l1.Address;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.Layer2;
import cz.cuni.mff.d3s.jdeeco.network.l2.PacketHeader;
import cz.cuni.mff.d3s.jdeeco.network.l2.ReceivedInfo;
import cz.cuni.mff.d3s.jdeeco.network.l2.Strategy;
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
		registry.registerMarshaller(PacketType.KNOWLEDGE, new SerializingMarshaller());

		// Instantiate layer
		l2Layer = new Layer2(layer1, registry);
	}

	/**
	 * Tests L2 packet marshaling availability and consistency
	 */
	@Test
	public void testL2PacketMarshalling() {
		// Create source packet
		L2Packet srcPacket = l2Layer.createPacket(new PacketHeader(PacketType.KNOWLEDGE), PAYLOAD);
		assertPayload(srcPacket.getObject());

		// Create destination packet from source packet binary data
		ReceivedInfo info = new ReceivedInfo(new LinkedList<L1Packet>(), 1, 1);
		L2Packet dstPacket = l2Layer.createPacket(new PacketHeader(PacketType.KNOWLEDGE), srcPacket.getData(), info);
		assertPayload(dstPacket.getObject());
	}

	/**
	 * Tests passing L2 packet via L2 layer to L1 layer
	 */
	@Test
	public void testL2PacketSending() {
		// Create source packet
		L2Packet srcPacket = l2Layer.createPacket(new PacketHeader(PacketType.KNOWLEDGE), PAYLOAD);
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
		Strategy strategy = Mockito.mock(Strategy.class);
		l2Layer.registerL2Strategy(strategy);

		ReceivedInfo info = new ReceivedInfo(new LinkedList<L1Packet>(), 1, 1);

		// Create source packet (created with data and received packet info)
		L2Packet srcPacket = l2Layer.createPacket(new PacketHeader(PacketType.KNOWLEDGE),
				registry.marshall(PacketType.KNOWLEDGE, PAYLOAD), info);
		assertPayload(srcPacket.getObject());

		// Process packet
		l2Layer.processL2Packet(srcPacket);

		// Check packet was processed by strategy
		Mockito.verify(strategy).processL2Packet(Matchers.eq(srcPacket));
	}
}
