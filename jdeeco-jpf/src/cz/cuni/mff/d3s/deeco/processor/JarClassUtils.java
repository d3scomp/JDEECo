package cz.cuni.mff.d3s.deeco.processor;

import java.io.File;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.processor.FileExtensionFilter;

/**
 * Utils class delivering functionality for class name retrieval either
 * from a jar file or a directory containing class definitions.
 * This class is used also by the SDE project.
 * 
 * @author Michal Kit
 * 
 */
public class JarClassUtils {
	
	/**
	 * Retrieves class names from a jar file.
	 * 
	 * @param jarPath path to the jar file.
	 * @return List of class names found in the given jar file.
	 */
	public static List<String> getClassNamesFromJar(String jarPath) {
		try {
			List<String> result = new LinkedList<String>();
			JarFile jarFile = null;
			try {
				jarFile = new JarFile(jarPath);
				Enumeration<JarEntry> enumeration = jarFile.entries();
				JarEntry je;
				JavaClass jc;
				while (enumeration.hasMoreElements()) {
					je = enumeration.nextElement();
					if (je.getName().endsWith(".class")) {
						try {
							jc = new ClassParser(jarPath, je.getName()).parse();
							result.add(jc.getClassName());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} finally {
				if (jarFile != null)
					jarFile.close();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves class names from a directory.
	 * 
	 * @param dir path to the directory containing class definitions.
	 * @return List of class names found in the given directory.
	 */
	public static List<String> getClassNamesFromDir(File dir) {
		List<String> result = new LinkedList<String>();
		ClassParser cp;
		if (dir != null && dir.isDirectory()) {
			File[] contents = dir.listFiles();
			if (contents != null && contents.length > 0) {
				FileExtensionFilter classFef = new FileExtensionFilter(".class");
				for (File f : contents) {
					if (f.isDirectory())
						result.addAll(getClassNamesFromDir(f));
					else if (classFef.accept(f.getParentFile(), f.getName())) {
						try {
							cp = new ClassParser(f.getAbsolutePath());
							result.add(cp.parse().getClassName());
							Log.i("File found: " + f.getName());
						} catch (Exception e) {
						}
					}
				}
			}
		}
		return result;
	}
}
