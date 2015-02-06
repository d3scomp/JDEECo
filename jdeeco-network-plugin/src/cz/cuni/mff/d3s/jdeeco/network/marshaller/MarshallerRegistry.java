package cz.cuni.mff.d3s.jdeeco.network.marshaller;

import java.util.*;

import cz.cuni.mff.d3s.jdeeco.network.PacketType;
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
	 * Registers marshaler for particular value type
	 * 
	 * @param type
	 *            Packet type
	 * @param marshaller
	 *            Marshaler implementation
	 */
	public void registerMarshaller(PacketType type, Marshaller marshaller) {
		marshallers.put(type, marshaller);
	}
}
