package cz.cuni.mff.d3s.deeco.processor;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.provider.ClassDEECoObjectProvider;

/**
 * A utility class for obtaining {@link ClassDEECoObjectProvider}.
 * 
 * @author Michal
 * 
 */
public class ClassProcessor {

	/**
	 * Returns {@link ClassDEECoObjectProvider} for the given class names,
	 * directory locations and class loader to be used for loading the classes.
	 * 
	 * @param classFiles
	 *            class names to be provided by the provider.
	 * @param dirURLs
	 *            location of the class definitions.
	 * @throws MalformedURLException
	 */
	public static ClassDEECoObjectProvider processClasses(
			List<String> classNames, List<URL> dirURLs) {
		return processClasses(classNames, dirURLs,
				ClassProcessor.class.getClassLoader());
	}

	/**
	 * Returns {@link ClassDEECoObjectProvider} for the given class names,
	 * directory locations and class loader to be used for loading the classes.
	 * 
	 * @param classFiles
	 *            class names to be provided by the provider.
	 * @param dirURLs
	 *            location of the class definitions.
	 * @throws MalformedURLException
	 */
	public static ClassDEECoObjectProvider processClasses(List<String> classes,
			List<URL> dirURLs, ClassLoader parentClassLoader) {
		ClassDEECoObjectProvider result = null;
		if (classes.size() > 0) {
			// dirURLs.addAll(Arrays.asList(((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs()));
			result = new ClassDEECoObjectProvider();
			URLClassLoader cl = new URLClassLoader(
					dirURLs.toArray(new URL[] {}), parentClassLoader);
			Class<?> clazz;
			for (String cs : classes) {
				try {
					clazz = Class.forName(cs, true, cl);
					result.addDEECoObjectClass(clazz);
				} catch (Exception e) {
					Log.e("Parsing exception", e);
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
