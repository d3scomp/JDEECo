package cz.cuni.mff.d3s.deeco.knowledge.local;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class TCCLObjectInputStream extends ObjectInputStream {

	protected TCCLObjectInputStream() throws IOException, SecurityException {
		super();
	}

	@Override
	public Class resolveClass(ObjectStreamClass desc) throws IOException,
			ClassNotFoundException {
		ClassLoader currentTCCL = null;
		try {
			currentTCCL = Thread.currentThread().getContextClassLoader();
			return currentTCCL.loadClass(desc.getName());
		} catch (Exception e) {}
		return super.resolveClass(desc);
	}

	public TCCLObjectInputStream(InputStream in) throws IOException {
		super(in);
	}

}
