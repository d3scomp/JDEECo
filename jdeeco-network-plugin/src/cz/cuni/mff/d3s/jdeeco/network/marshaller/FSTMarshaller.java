package cz.cuni.mff.d3s.jdeeco.network.marshaller;

import org.nustaq.serialization.FSTConfiguration;

public class FSTMarshaller implements Marshaller {
	static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
	
	@Override
	public byte[] marshall(Object data) throws Exception {
		return conf.asByteArray(data);
	}

	@Override
	public Object unmashall(byte[] data) throws Exception {
		return conf.asObject(data);
	}
}
