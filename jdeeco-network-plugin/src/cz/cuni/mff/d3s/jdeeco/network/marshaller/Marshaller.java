package cz.cuni.mff.d3s.jdeeco.network.marshaller;

public interface Marshaller {
	public byte[] marshall(Object data) throws Exception;
	public Object unmashall(byte[] data) throws Exception;
}
