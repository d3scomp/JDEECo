package cz.cuni.mff.d3s.deeco.processor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassParser;

import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;

public class ClassProcessor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("Wrong parameterTypes!");
			return;
		}
		FileExtensionFilter classFef = new FileExtensionFilter(".class"), jarFef = new FileExtensionFilter(".jar");
		File tFile;
		List<String> classes = new LinkedList<String>();
		List<URL> dirURLs = new LinkedList<URL>();
		for (int i = 0; i < args.length; i++) {
			try {
				tFile = new File(args[i]);
				if (tFile != null) {
					if (tFile.isDirectory()) {
						dirURLs.add(tFile.toURI().normalize().toURL());
						classes.addAll(getClassNamesFromDir(tFile, classFef));
					} else if (jarFef.accept(tFile.getParentFile(), tFile.getName())) {
						dirURLs.add(tFile.toURI().normalize().toURL());
						classes.addAll(getClassNamesFromJar(tFile, classFef));
					}
				}
			} catch (Exception e) {
				System.out.println("Parsing exception: ");
				//e.printStackTrace();
			}
		}
		processClassFiles(classes, dirURLs);
	}

	/**
	 * @param classFiles
	 * @param dirURLs
	 * @throws MalformedURLException 
	 */
	private static void processClassFiles(List<String> classes,
			List<URL> dirURLs) {
		if (classes.size() > 0) {
			List<SchedulableProcessWrapper> sp = new LinkedList<SchedulableProcessWrapper>();
			List<ComponentKnowledge> ik = new LinkedList<ComponentKnowledge>();
			ComponentKnowledge initialKnowledge;
			//dirURLs.addAll(Arrays.asList(((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs()));
			URLClassLoader cl = new URLClassLoader(dirURLs.toArray(new URL[] {}));
			Class<?> clazz;
			ComponentParser componentParser = new ComponentParser();
			EnsembleParser ensembleParser = new EnsembleParser();
			for (String cs : classes) {
				try {
					clazz = Class.forName(cs ,true, cl);
					if (componentParser.isComponentDefinition(clazz)) {
						initialKnowledge = componentParser
								.extractInitialKnowledge(clazz);
						if (initialKnowledge != null) {
							addComponents(sp, componentParser, clazz,
									initialKnowledge.id);
							ik.add(initialKnowledge);
						}
					} else if (ensembleParser.isEnsembleDefinition(clazz)) {
						addEnsemble(sp, ensembleParser, clazz);
					}
				} catch (Exception e) {
					System.out.println("Parsing exception:");
					e.printStackTrace();
				}
			}
			ParsedObjectWriter pow = new ParsedObjectWriter();
			pow.write(sp, ik);
		}
	}

	private static void addComponents(List<SchedulableProcessWrapper> sp,
			ComponentParser cp, Class<?> clazz, String componentId) {
		sp.addAll(SchedulableComponentProcessWrapper.wrapComponentProcess(
				clazz, cp.extractComponentProcess(clazz, componentId)));
	}

	private static void addEnsemble(List<SchedulableProcessWrapper> sp,
			EnsembleParser ep, Class<?> clazz) {
		sp.add(SchedulableEnsembleProcessWrapper.wrapEnsembleProcess(clazz,
				ep.extractEnsembleProcess(clazz)));
	}
	
	private static List<String> getClassNamesFromJar(File jar, FileExtensionFilter fef) {
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
			} catch (Exception e) {
				
			}
		}
		return result;
	}
	
	private static List<String> getClassNamesFromDir(File dir, FileExtensionFilter fef) {
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
}
