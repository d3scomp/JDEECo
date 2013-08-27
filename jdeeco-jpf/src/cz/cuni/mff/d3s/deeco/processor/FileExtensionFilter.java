package cz.cuni.mff.d3s.deeco.processor;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Generic filter class used to filter files by their extensions.
 * 
 * @author Michal Kit
 * 
 */
public class FileExtensionFilter implements FilenameFilter {

	private String ext = "*";

	public FileExtensionFilter(String ext) {
		this.ext = ext;
	}

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File dir, String name) {
		if (name.endsWith(ext))
			return true;
		return false;
	}
}
