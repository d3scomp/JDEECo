package cz.cuni.mff.d3s.deeco.processor;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.logging.Log;

import static cz.cuni.mff.d3s.deeco.processor.JarClassUtils.getClassNamesFromJar;
import static cz.cuni.mff.d3s.deeco.processor.JarClassUtils.getClassNamesFromDir;

// TODO: Comment is missing

public class ClassFinder {
	private final List<String> classes;
	private final List<URL> dirURLs;
	
	public ClassFinder() {
		classes = new LinkedList<String>();
		dirURLs = new LinkedList<URL>();
	}
	
	public void resolve(String [] paths) {
		File tFile;
		FileExtensionFilter jarFef = new FileExtensionFilter(".jar");
		for (int i = 0; i < paths.length; i++) {
			try {
				tFile = new File(paths[i]);
				if (tFile != null) {
					if (tFile.isDirectory()) {
						if (classes.addAll(getClassNamesFromDir(tFile)))
							dirURLs.add(tFile.toURI().normalize().toURL());
					} else if (jarFef.accept(tFile.getParentFile(), tFile.getName())) {
						if (classes.addAll(getClassNamesFromJar(paths[i])))
							dirURLs.add(tFile.toURI().normalize().toURL());
					}
				}
			} catch (Exception e) {
				Log.e("Parsing exception",e);
			}
		}
	}

	public List<String> getClasses() {
		return classes;
	}

	public List<URL> getDirURLs() {
		return dirURLs;
	}
}
