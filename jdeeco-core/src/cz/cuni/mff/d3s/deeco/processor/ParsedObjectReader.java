package cz.cuni.mff.d3s.deeco.processor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.logging.LoggerFactory;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;

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

	public AbstractDEECoObjectProvider read() {
		AbstractDEECoObjectProvider result = null;
		try {
			ObjectInput oi = null;
			try {
				InputStream fi = new FileInputStream(fileName);
				InputStream bi = new BufferedInputStream(fi);
				oi = new ObjectInputStream(bi);
				result = (AbstractDEECoObjectProvider) oi.readObject();
			} finally {
				if (oi != null)
					oi.close();
			}
		} catch (Exception e) {
			LoggerFactory.getLogger().severe("Error when reading",e);
			return result;
		}
		return result;
	}
}
