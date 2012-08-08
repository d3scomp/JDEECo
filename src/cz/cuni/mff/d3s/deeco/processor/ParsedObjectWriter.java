package cz.cuni.mff.d3s.deeco.processor;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;

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

	public boolean write(List<SchedulableProcessWrapper> spw,
			List<ComponentKnowledge> ck) {
		try {
			ObjectOutput oo = null;
			try {
				OutputStream fs = new FileOutputStream(fileName);
				OutputStream bo = new BufferedOutputStream(fs);
				oo = new ObjectOutputStream(bo);
				oo.writeObject(spw);
				oo.writeObject(ck);

			} finally {
				if (oo != null)
					oo.close();
			}
		} catch (Exception e) {
			System.out.println("Error when writing");
			return false;
		}
		return true;
	}
}
