package cz.cuni.mff.d3s.deeco.processor;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import org.apache.bcel.classfile.ClassParser;

import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableComponentProcessCreator;
import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;

public class ClassProcessor {

	public static void processClassFiles(List<String> classNames, List<URL> dirURLs, ProcessedHolder ph) {
		processClassFiles(classNames, dirURLs, ph, null);
	}
	
	public static void processClassFiles(List<String> classNames,
			List<URL> dirURLs, ProcessedHolder ph, ClassLoader parentClassLoader) {
		if (classNames.size() > 0) {		

	/**
	 * @param classFiles
	 * @param dirURLs
	 * @throws MalformedURLException 
	 */
	private static void processClassFiles(List<String> classes,
			List<URL> dirURLs) {
		if (classes.size() > 0) {
			List<SchedulableProcessCreator> sp = new LinkedList<SchedulableProcessCreator>();
			List<ComponentKnowledge> ik = new LinkedList<ComponentKnowledge>();
			ComponentKnowledge initialKnowledge;
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

	private static void addComponents(List<SchedulableProcessCreator> sp,
			ComponentParser cp, Class<?> clazz, String componentId) {
		sp.addAll(cp.extractComponentProcess(clazz, componentId));
	}

	private static void addEnsemble(List<SchedulableProcessCreator> sp,
			EnsembleParser ep, Class<?> clazz) {
		sp.add(ep.extractEnsembleProcess(clazz));
	}
	
}
