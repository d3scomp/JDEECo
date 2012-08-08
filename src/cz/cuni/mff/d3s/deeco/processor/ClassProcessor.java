package cz.cuni.mff.d3s.deeco.processor;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
		FileExtensionFilter fef = new FileExtensionFilter(".class");
		File tFile;
		List<File> classFiles = new LinkedList<File>();
		List<URL> dirURLs = new LinkedList<URL>();
		for (int i = 0; i < args.length; i++) {
			try {
				tFile = new File(args[i]);
				if (tFile != null) {
					if (tFile.isDirectory()) {
						classFiles.addAll(Arrays.asList(tFile.listFiles(fef)));
						dirURLs.add(tFile.toURI().toURL());
					} else if (fef.accept(tFile.getParentFile(),
							tFile.getName())) {
						classFiles.add(tFile);
						dirURLs.add(tFile.getParentFile().toURI().toURL());
					}
				}
			} catch (Exception e) {
				System.out.println("Parsing exception!");
			}
		}
		processClassFiles(classFiles, dirURLs);
	}

	/**
	 * @param classFiles
	 * @param dirURLs
	 */
	private static void processClassFiles(List<File> classFiles,
			List<URL> dirURLs) {
		if (classFiles.size() > 0) {
			ClassParser cp;
			List<SchedulableProcessWrapper> sp = new LinkedList<SchedulableProcessWrapper>();
			List<ComponentKnowledge> ik = new LinkedList<ComponentKnowledge>();
			ComponentKnowledge initialKnowledge;
			ClassLoader cl = new URLClassLoader(dirURLs.toArray(new URL[] {}));
			Class<?> clazz;
			ComponentParser componentParser = new ComponentParser();
			EnsembleParser ensembleParser = new EnsembleParser();
			for (File f : classFiles) {
				try {
					cp = new ClassParser(f.getAbsolutePath());
					clazz = cl.loadClass(cp.parse().getClassName());
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
					System.out.println("Parsing exception!");
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
}
