package cz.cuni.mff.d3s.jdeeco.network.marshaller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializingMarshaller implements Marshaller {
	public byte[] marshall(Object data) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream serializer = new ObjectOutputStream(stream);
		serializer.writeObject(data);
		return stream.toByteArray();
	}

	public Object unmashall(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream stream = new ByteArrayInputStream(data);
		ObjectInputStream deserializer = new ObjectInputStream(stream);
		return deserializer.readObject();
	}
}
