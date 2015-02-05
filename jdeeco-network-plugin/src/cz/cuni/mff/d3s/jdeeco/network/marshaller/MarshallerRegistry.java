package cz.cuni.mff.d3s.jdeeco.network.marshaller;

import cz.cuni.mff.d3s.jdeeco.network.PacketType;

public class MarshallerRegistry {
	byte[] marshall(PacketType type, Object data) {
		return null;
	}
	
	Object unmarshall(int type, byte[] data) {
		return null;
	}
	
	PacketType resolvePacketType(int type) {
		throw new RuntimeException();
	}
	
	void registerMarshaller(PacketType type, Marshaller marshaller) {
		throw new RuntimeException();
	}
}
