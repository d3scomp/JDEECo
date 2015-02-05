package cz.cuni.mff.d3s.jdeeco.network;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.jdeeco.network.PacketTypes.GrouperPacketType;
import cz.cuni.mff.d3s.jdeeco.network.PacketTypes.KnowledgePacketType;
import cz.cuni.mff.d3s.jdeeco.network.exceptions.UnregistredPacketType;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.MarshallerRegistry;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.SerializingMarshaller;

/**
 * Tests data marshaling and related code
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class MarshalingTest {
	// Registry used for testing
	private MarshallerRegistry registry;

	// Test pay-load for marshaling
	private final String PAYLOAD = "payload";

	/**
	 * Initializes registry and registers knowledge marshaler
	 */
	@Before
	public void initializeMarshallingRegistry() {
		// Create registry
		registry = new MarshallerRegistry();

		// Register marshaler
		SerializingMarshaller marshaller = new SerializingMarshaller();
		registry.registerMarshaller(new KnowledgePacketType(), marshaller);
	}

	/**
	 * Tests whenever packet type resolution based on the integer value works
	 * @throws UnregistredPacketType
	 */
	@Test
	public void testPacketTypeResolution() throws UnregistredPacketType {
		// Try to resolve knowledge packet type by int value
		assertEquals(new KnowledgePacketType(), registry.resolvePacketType(KnowledgePacketType.instance.value()));
	}

	@Test
	public void testEncodeDecodeByValue() throws UnregistredPacketType {
		byte[] data = registry.marshall(new KnowledgePacketType(), PAYLOAD);
		Object obj = registry.unmarshall(new KnowledgePacketType().value(), data);
		assertEquals(PAYLOAD, obj);
	}

	@Test
	public void testEncodeDecodeByType() throws IOException, ClassNotFoundException, UnregistredPacketType {
		byte[] data = registry.marshall(new KnowledgePacketType(), PAYLOAD);
		Object obj = registry.unmarshall(new KnowledgePacketType(), data);
		assertEquals(PAYLOAD, obj);
	}

	@Test
	public void testPacketTypeVariability() {
		// Test if value for knowledge packet type is zero (knowledge is special)
		assertEquals(0, KnowledgePacketType.instance.value());
		// Test if Grouper packet type is not zero (should be random abased on the GrouperPacketType class name)
		assertNotEquals(0, GrouperPacketType.instance.value());
	}
}
