package cz.cuni.mff.d3s.jdeeco.network.exceptions;

public class UnregistredPacketType extends Exception {
	private int type;
	
	protected UnregistredPacketType(int type) {
		this.type = type;
	}
	
	@Override
	public String getMessage() {
		return String.format("Packet type %d is has no registred for marshalling.", type);
	}
}
