package cz.cuni.mff.d3s.jdeeco.network.marshaller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.jdeeco.network.exceptions.UnregistredPacketType;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2PacketType;

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

	// Complex test pay-load for marshaling
	private final Map<Integer, String> COMPLEX_PAYLOAD = new HashMap<Integer, String>();

	public MarshallingTest() {
		COMPLEX_PAYLOAD.put(1, "One");
		COMPLEX_PAYLOAD.put(2, "Two");
		COMPLEX_PAYLOAD.put(3, "Three");
		COMPLEX_PAYLOAD.put(4, "Four");
		COMPLEX_PAYLOAD.put(5, "Five");
	}

	private void assertSimplePayload(Object payload) {
		assertEquals(SIMPLE_PAYLOAD, payload);
	}

	private void assertComplexPayload(Object payload) {
		@SuppressWarnings("unchecked")
		Map<Integer, String> payloadMap = (Map<Integer, String>) payload;

		assertEquals(COMPLEX_PAYLOAD.size(), payloadMap.size());
		assertTrue("Checks whenever the COMPLEX_PAYLOAD (hashmap) contains all entries", COMPLEX_PAYLOAD.entrySet()
				.containsAll(payloadMap.entrySet()));
	}

	/**
	 * Initializes registry and registers knowledge marshaler
	 */
	@Before
	public void initializeMarshallingRegistry() {
		// Create registry
		registry = new MarshallerRegistry();

		// Register marshaler
		Marshaller marshaller = new FSTMarshaller();
		registry.registerMarshaller(L2PacketType.KNOWLEDGE, marshaller);
	}

	/**
	 * Tests encode and decode of pay-load using knowledge marshaler
	 * 
	 * @throws UnregistredPacketType
	 */
	@Test
	public void testEncodeDecodeSimple() throws UnregistredPacketType {
		byte[] data = registry.marshall(L2PacketType.KNOWLEDGE, SIMPLE_PAYLOAD);
		Object obj = registry.unmarshall(L2PacketType.KNOWLEDGE, data);
		assertSimplePayload(obj);
	}
	
	/**
	 * Tests encode and decode of complex pay-load using knowledge marshaler
	 * 
	 * @throws UnregistredPacketType
	 */
	@Test
	public void testEncodeDecodeComplex() throws UnregistredPacketType {
		byte[] data = registry.marshall(L2PacketType.KNOWLEDGE, COMPLEX_PAYLOAD);
		Object obj = registry.unmarshall(L2PacketType.KNOWLEDGE, data);
		assertComplexPayload(obj);
	}
}
