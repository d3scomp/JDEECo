package cz.cuni.mff.d3s.jdeeco.network;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.jdeeco.network.marshaller.MarshallerRegistry;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.SerializingMarshaller;

public class MarshalingTest {
	@Test
	public void test() {
		assertTrue(true);
	}

	// Registry used for testing
	private MarshallerRegistry registry;
	
	// Test pay-load for marshaling
	private final String PAYLOAD = "payload";

	@Before
	public void initializeMarshallingRegistry() {
		// Create registry
		registry = new MarshallerRegistry();

		// Register marshaler
		SerializingMarshaller marshaller = new SerializingMarshaller();
		registry.registerMarshaller(new KnowledgePacketType(), marshaller);
	}

	@Test
	public void testPacketTypeResolution() {
		// Try to resolve knowledge packet type by int value
		assertEquals(new KnowledgePacketType().ordinal(),
				registry.resolvePacketType(new KnowledgePacketType().ordinal()));
	}

	@Test
	public void testEncodeDecodeByValue() {
		byte[] data = registry.marshall(new KnowledgePacketType(), PAYLOAD);
		Object obj = registry.unmarshall(new KnowledgePacketType().ordinal(), data);
		assertEquals(PAYLOAD, obj);
	}
	
	@Test
	public void testEncodeDecodeByType() {
		byte[] data = registry.marshall(new KnowledgePacketType(), PAYLOAD);
		Object obj = registry.unmarshall(new KnowledgePacketType(), data);
		assertEquals(PAYLOAD, obj);
	}
}
