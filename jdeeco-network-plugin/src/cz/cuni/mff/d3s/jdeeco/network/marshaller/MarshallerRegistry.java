package cz.cuni.mff.d3s.jdeeco.network.marshaller;

import cz.cuni.mff.d3s.jdeeco.network.PacketType;

public class MarshallerRegistry {
	public byte[] marshall(PacketType type, Object data) {
		return null;
	}
	
	public Object unmarshall(int type, byte[] data) {
		return unmarshall(resolvePacketType(type), data);
	}
	
	public Object unmarshall(PacketType type, byte[] data) {
		return null;
	}
	
	public PacketType resolvePacketType(int type) {
		throw new RuntimeException();
	}
	
	public void registerMarshaller(PacketType type, Marshaller marshaller) {
		throw new RuntimeException();
	}
}
