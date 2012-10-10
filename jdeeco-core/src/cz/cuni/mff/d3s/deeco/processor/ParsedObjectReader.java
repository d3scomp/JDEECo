package cz.cuni.mff.d3s.deeco.processor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;

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

	public boolean read(List<SchedulableProcessCreator> spw, List<ComponentKnowledge> ck) {
		try {
			ObjectInput oi = null;
			try {
				InputStream fi = new FileInputStream(fileName);
				InputStream bi = new BufferedInputStream(fi);
				oi = new ObjectInputStream(bi);
				spw.addAll((List<SchedulableProcessCreator>) oi.readObject());
				ck.addAll((List<ComponentKnowledge>) oi.readObject());
			} finally {
				if (oi != null)
					oi.close();
			}
		} catch (Exception e) {
			System.out.println("Error when reading");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
