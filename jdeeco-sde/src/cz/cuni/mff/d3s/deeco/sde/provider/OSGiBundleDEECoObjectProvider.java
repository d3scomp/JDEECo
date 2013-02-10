package cz.cuni.mff.d3s.deeco.sde.provider;

import java.util.Dictionary;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;

import cz.cuni.mff.d3s.deeco.processor.ComponentParser;
import cz.cuni.mff.d3s.deeco.processor.EnsembleParser;
import cz.cuni.mff.d3s.deeco.provider.ClassDEECoObjectProvider;

public class OSGiBundleDEECoObjectProvider extends ClassDEECoObjectProvider {
	
	private Bundle thisBundle;
	
	public void activate(ComponentContext context) {
		System.out.println("activated");
		thisBundle = context.getBundleContext().getBundle();
		Dictionary<String, ?> properties = context.getProperties();
		extractRawFromString((String) properties.get("contents"));
    }
	
	
	private void extractRawFromString(String classes) {
		if (classes != null && !classes.equals("")) {
			Class<?> clazz;
			for (String sClass : classes.split(",")) {
				try {
					clazz = thisBundle.loadClass(sClass);
					if (clazz != null) {
						if(ComponentParser.isComponentDefinition(clazz))
							rawComponents.add(clazz);
						else if (EnsembleParser.isEnsembleDefinition(clazz))
							rawEnsembles.add(clazz);
					}
				} catch (ClassNotFoundException e) {
					System.out.println("Class loading exception: " + e.getMessage());
				}
			}
		}
	}
	
	@Override
	public ClassLoader getContextClassLoader() {
		if (!rawComponents.isEmpty())
			return rawComponents.get(0).getClassLoader();
		if (!rawEnsembles.isEmpty())
			return rawEnsembles.get(0).getClassLoader();
		return null;
	}
}