package cz.cuni.mff.d3s.deeco.sde.provider;

import java.util.Dictionary;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;

import cz.cuni.mff.d3s.deeco.provider.ClassDEECoObjectProvider;

public class OSGiBundleDEECoObjectProvider extends ClassDEECoObjectProvider {
	
	private Bundle thisBundle;
	
	public void activate(ComponentContext context) {
		System.out.println("activated");
		thisBundle = context.getBundleContext().getBundle();
		Dictionary<String, ?> properties = context.getProperties();
		extractRawFromString((String) properties.get("components"), rawComponents);
		extractRawFromString((String) properties.get("ensembles"), rawEnsembles);
    }
	
	
	private void extractRawFromString(String classes, List<Class<?>> collection) {
		if (classes != null && !classes.equals("")) {
			Class<?> clazz;
			for (String sClass : classes.split(",")) {
				try {
					clazz = thisBundle.loadClass(sClass);
					if (clazz != null) {
						collection.add(clazz);
					}
				} catch (ClassNotFoundException e) {
					System.out.println("Class loading exception: " + e.getMessage());
				}
			}
		}
	}
}