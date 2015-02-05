package cz.cuni.mff.d3s.jdeeco.network.marshaller;

import java.io.IOException;

public interface Marshaller {
	public byte[] marshall(Object data) throws IOException;
	public Object unmashall(byte[] data) throws IOException, ClassNotFoundException;
}
