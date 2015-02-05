package cz.cuni.mff.d3s.jdeeco.network.marshaller;

public interface Marshaller {
	byte[] marshall(Object data);
	Object unmashall(byte[] data);
}
