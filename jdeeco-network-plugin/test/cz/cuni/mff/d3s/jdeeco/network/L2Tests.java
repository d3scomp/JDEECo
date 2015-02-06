package cz.cuni.mff.d3s.jdeeco.network;

import org.junit.*;

import static org.junit.Assert.*;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.Layer;
import cz.cuni.mff.d3s.jdeeco.network.l2.PacketHeader;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.MarshallerRegistry;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.SerializingMarshaller;

/**
 * Tests for layer 2 jDEECo networking
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class L2Tests {
	private Layer l2Layer;

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
		MarshallerRegistry registry = new MarshallerRegistry();
		registry.registerMarshaller(PacketTypes.KNOWLEDGE, new SerializingMarshaller());

		// Instantiate layer
		l2Layer = new Layer(registry);
	}

	/**
	 * Tests L2 packet marshaling availability and consistency
	 */
	@Test
	public void testL2PacketMarshalling() {
		// Create source packet
		L2Packet srcPacket = l2Layer.createPacket(new PacketHeader(PacketTypes.KNOWLEDGE), PAYLOAD);
		assertPayload(srcPacket.getObject());

		// Create destination packet from source packet binary data
		L2Packet dstPacket = l2Layer.createPacket(new PacketHeader(PacketTypes.KNOWLEDGE), srcPacket.getData());
		assertPayload(dstPacket.getObject());
	}
}
