package cz.cuni.mff.d3s.jdeeco.network;

public class PacketType {
	public String name() {
		return this.getClass().getName();
	}
	
	public int ordinal() {
		throw new RuntimeException();
	}
	
	@Override
	public int hashCode() {
		return ordinal();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PacketType) {
			return ((PacketType)obj).ordinal() == ordinal();
		} else {
			return super.equals(obj);
		}
	}
}
