package cz.cuni.mff.d3s.jdeeco.network;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.jdeeco.network.exceptions.UnregistredPacketType;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.MarshallerRegistry;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.SerializingMarshaller;

/**
 * Tests data marshaling and related code
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class MarshallingTest {
	// Registry used for testing
	private MarshallerRegistry registry;

	// Test pay-load for marshaling
	private final String SIMPLE_PAYLOAD = "payload";

	/**
	 * Initializes registry and registers knowledge marshaler
	 */
	@Before
	public void initializeMarshallingRegistry() {
		// Create registry
		registry = new MarshallerRegistry();

		// Register marshaler
		SerializingMarshaller marshaller = new SerializingMarshaller();
		registry.registerMarshaller(PacketTypes.KNOWLEDGE, marshaller);
	}

	/**
	 * Tests encode and decode of payload using knowledge marshaler
	 * 
	 * @throws UnregistredPacketType
	 */
	@Test
	public void testEncodeDecodeByValue() throws UnregistredPacketType {
		byte[] data = registry.marshall(PacketTypes.KNOWLEDGE, SIMPLE_PAYLOAD);
		Object obj = registry.unmarshall(PacketTypes.KNOWLEDGE, data);
		assertEquals(SIMPLE_PAYLOAD, obj);
	}
}
