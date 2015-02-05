package cz.cuni.mff.d3s.jdeeco.network.marshaller;

public interface Marshaller {
	public byte[] marshall(Object data);
	public Object unmashall(byte[] data);
}
