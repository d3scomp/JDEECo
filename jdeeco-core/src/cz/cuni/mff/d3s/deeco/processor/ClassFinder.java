package cz.cuni.mff.d3s.deeco.processor;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassParser;

public class ClassFinder {
	private final List<String> classes;
	private final List<URL> dirURLs;
	private final FileExtensionFilter classFef, jarFef;
	
	public ClassFinder() {
		classes = new LinkedList<String>();
		dirURLs = new LinkedList<URL>();
		classFef = new FileExtensionFilter(".class");
		jarFef = new FileExtensionFilter(".jar");
	}
	
	public void resolve(String [] paths) {
		File tFile;
		for (int i = 0; i < paths.length; i++) {
			try {
				tFile = new File(paths[i]);
				if (tFile != null) {
					if (tFile.isDirectory()) {
						if (classes.addAll(getClassNamesFromDir(tFile, classFef)))
							dirURLs.add(tFile.toURI().normalize().toURL());
					} else if (jarFef.accept(tFile.getParentFile(), tFile.getName())) {
						if (classes.addAll(getClassNamesFromJar(tFile, classFef)))
							dirURLs.add(tFile.toURI().normalize().toURL());
					}
				}
			} catch (Exception e) {
				System.out.println("Parsing exception: ");
			}
		}
	}
	
	private List<String> getClassNamesFromJar(File jar, FileExtensionFilter fef) {
		List<String> result = new LinkedList<String>();
		if (jar != null && jar.isFile()) {
			try {
				JarFile jf = new JarFile(jar);
				Enumeration<JarEntry> enumeration = jf.entries();
				JarEntry je;
				String name;
				while (enumeration.hasMoreElements()) {
					je = enumeration.nextElement();
					name = je.getName();
					if (name.endsWith(".class")) {
						name = name.replace(".class", "");
						name = name.replaceAll("/", ".");
						result.add(name);
						System.out.println("Found : " + name);
					}
				}
				jf.close();
			} catch (Exception e) {
				
			}
		}
		return result;
	}
	
	private List<String> getClassNamesFromDir(File dir, FileExtensionFilter fef) {
		List<String> result = new LinkedList<String>();
		ClassParser cp;
		if (dir != null && dir.isDirectory()) {
			File [] contents = dir.listFiles();
			if (contents != null && contents.length > 0) {
				for (File f : contents) {
					if (f.isDirectory())
						result.addAll(getClassNamesFromDir(f, fef));
					else if (fef.accept(f.getParentFile(), f.getName())) {
						try {
							cp = new ClassParser(f.getAbsolutePath());
							result.add(cp.parse().getClassName());
							System.out.println("File found: " + f.getName());
						} catch (Exception e) {	
						}
					}
				}
			}
		}
		return result;
	}

	public List<String> getClasses() {
		return classes;
	}

	public List<URL> getDirURLs() {
		return dirURLs;
	}
}
