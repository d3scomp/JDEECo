package cz.cuni.mff.d3s.deeco.processor;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.provider.DEECoObjectProvider;

/**
 * Serialized object provider writer. This class is used to write an
 * {@link DEECoObjectProvider} instance to a file. Used for separating
 * the reflection part from the runtime part, which is imposed by JPF usage.
 * 
 * @author Michal
 * 
 */
public class ParsedObjectWriter {

	private String fileName;

	public ParsedObjectWriter() {
		fileName = ConstantKeys.BIN_FILE_NAME;
	}

	public ParsedObjectWriter(String fileName) {
		if (fileName == null || fileName.equals(""))
			this.fileName = ConstantKeys.BIN_FILE_NAME;
		else
			this.fileName = fileName;
	}

	/**
	 * Writes a {@link ClassDEECoObjectProvider} instance to a file.
	 * 
	 * @param cdop a provider to be written.
	 * @return True in case of the successful write operation. False otherwise.
	 */
	public boolean write(DEECoObjectProvider dop) {
		try {
			ObjectOutput oo = null;
			try {
				OutputStream fs = new FileOutputStream(fileName);
				OutputStream bo = new BufferedOutputStream(fs);
				oo = new ObjectOutputStream(bo);
				oo.writeObject(dop);
			} finally {
				if (oo != null)
					oo.close();
			}
		} catch (Exception e) {
			Log.e("Error when writing", e);
			return false;
		}
		return true;
	}
}
