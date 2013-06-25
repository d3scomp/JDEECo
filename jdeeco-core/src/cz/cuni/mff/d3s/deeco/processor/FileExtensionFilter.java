package cz.cuni.mff.d3s.deeco.processor;

import java.io.File;
import java.io.FilenameFilter;

/*
 * TODO: Comment is missing
 * This class should be an inner private class of ClassFinder
 */

public class FileExtensionFilter implements FilenameFilter {

	private String ext = "*";

	public FileExtensionFilter(String ext) {
		this.ext = ext;
	}

	@Override
	public boolean accept(File dir, String name) {
		if (name.endsWith(ext))
			return true;
		return false;
	}
}
