package cz.cuni.mff.d3s.jdeeco.network.marshaller;

import java.util.*;

import cz.cuni.mff.d3s.jdeeco.network.PacketTypes.PacketType;
import cz.cuni.mff.d3s.jdeeco.network.exceptions.MarshallingException;
import cz.cuni.mff.d3s.jdeeco.network.exceptions.UnregistredPacketType;

/**
 * Registry for marshalers based on the packet type
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class MarshallerRegistry {
	private Map<PacketType, Marshaller> marshallers = new HashMap<PacketType, Marshaller>();
	private Map<Integer, PacketType> packetTypes = new HashMap<Integer, PacketType>();

	/**
	 * Encodes object according to type into binary data
	 * 
	 * @param type
	 *            Packet type to determine marshaler
	 * @param data
	 *            Data to be encoded
	 * @return Encoded data representing source object
	 */
	public byte[] marshall(PacketType type, Object data) throws UnregistredPacketType {
		Marshaller marshaller = marshallers.get(type);
		if (marshaller == null) {
			throw new UnregistredPacketType(type);
		}
		try {
			return marshaller.marshall(data);
		} catch (Exception e) {
			throw new MarshallingException(e);
		}
	}

	/**
	 * Decodes binary data to object according to type
	 * 
	 * @param type
	 *            Packet type value to determine marshaler
	 * @param data
	 *            Binary data to be decoded into object
	 * @return Decoded object
	 */
	public Object unmarshall(int type, byte[] data) throws UnregistredPacketType, MarshallingException {
		return unmarshall(resolvePacketType(type), data);
	}

	/**
	 * Decodes binary data to object according to type
	 * 
	 * @param type
	 *            Packet type to determine marshaler
	 * @param data
	 *            Binary data to be decoded into object
	 * @return Decoded object
	 */
	public Object unmarshall(PacketType type, byte[] data) {
		Marshaller marshaller = marshallers.get(type);
		if (marshaller == null) {
			throw new UnregistredPacketType(type);
		}
		try {
			return marshaller.unmashall(data);
		} catch (Exception e) {
			throw new MarshallingException(e);
		}
	}

	/**
	 * Resolves packet type based on the packet type integer value
	 * 
	 * @param value
	 *            Integer value to be used to determine the packet type
	 * @return Packet type matching the value specified
	 */
	public PacketType resolvePacketType(final Integer value) {
		PacketType type = packetTypes.get(value);

		if (type == null) {
			throw new UnregistredPacketType(value);
		}

		return type;
	}

	/**
	 * Registers marshaler for particular value type
	 * 
	 * @param type
	 *            Packet type
	 * @param marshaller
	 *            Marshaler implementation
	 */
	public void registerMarshaller(PacketType type, Marshaller marshaller) {
		marshallers.put(type, marshaller);
		packetTypes.put(type.value(), type);
	}
}
