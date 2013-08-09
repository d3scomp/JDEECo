package cz.cuni.mff.d3s.deeco.processor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.provider.DEECoObjectProvider;

/**
 * Serialized object provider reader. This class is used to extract
 * {@link DEECoObjectProvider} instance from the file.
 * 
 * @author Michal Kit
 * 
 */
public class ParsedObjectReader {

	private String fileName;

	public ParsedObjectReader() {
		fileName = ConstantKeys.BIN_FILE_NAME;
	}

	public ParsedObjectReader(String fileName) {
		if (fileName == null || fileName.equals(""))
			this.fileName = ConstantKeys.BIN_FILE_NAME;
		else
			this.fileName = fileName;
	}

	/**
	 * Reads the {@link DEECoObjectProvider} from the file.
	 * 
	 * @return retrieved {@link DEECoObjectProvider} object from the file.
	 */
	public DEECoObjectProvider read() {
		DEECoObjectProvider result = null;
		try {
			ObjectInput oi = null;
			try {
				InputStream fi = new FileInputStream(fileName);
				InputStream bi = new BufferedInputStream(fi);
				oi = new ObjectInputStream(bi);
				result = (DEECoObjectProvider) oi.readObject();
			} finally {
				if (oi != null)
					oi.close();
			}
		} catch (Exception e) {
			Log.e("Error when reading", e);
			return result;
		}
		return result;
	}
}
