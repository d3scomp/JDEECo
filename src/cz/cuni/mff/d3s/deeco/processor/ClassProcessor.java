package cz.cuni.mff.d3s.deeco.processor;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

public class ClassProcessor {

	public static void processClassFiles(List<String> classNames, List<URL> dirURLs, ProcessedHolder ph) {
		processClassFiles(classNames, dirURLs, ph, null);
	}
	
	public static void processClassFiles(List<String> classNames,
			List<URL> dirURLs, ProcessedHolder ph, ClassLoader parentClassLoader) {
		if (classNames.size() > 0) {		
			//dirURLs.addAll(Arrays.asList(((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs()));
			URLClassLoader cl = new URLClassLoader(dirURLs.toArray(new URL[] {}), parentClassLoader);
			Class<?> clazz;
			for (String cs : classNames) {
				try {
					clazz = Class.forName(cs ,true, cl);
					ph.parseClass(clazz);
				} catch (Exception e) {
					System.out.println("Parsing exception:");
					e.printStackTrace();
				}
			}
		}
	}
	
}
