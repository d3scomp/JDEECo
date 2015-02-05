package cz.cuni.mff.d3s.jdeeco.network.exceptions;

import cz.cuni.mff.d3s.jdeeco.network.PacketType;

public class UnregistredPacketType extends Exception {
	private Integer value;
	private PacketType type;
	
	public UnregistredPacketType(int value) {
		this.value = value;
	}
	
	public UnregistredPacketType(PacketType type) {
		this.type = type;
	}
	
	@Override
	public String getMessage() {
		if(value != null) {
			return String.format("Packet type %d is has no registred for marshalling.", value);
		}
		
		if(type != null)
			return String.format("Packet type %s is has no registred for marshalling.", type);
		
		return String.format("Packet type has not been registred for marshalling.");
	}
}
