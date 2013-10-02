package cz.cuni.mff.d3s.deeco.model.provider;

import static cz.cuni.mff.d3s.deeco.processor.ComponentParser.extractComponentProcesses;
import static cz.cuni.mff.d3s.deeco.processor.EnsembleParser.extractEnsembleProcess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.Component;
import cz.cuni.mff.d3s.deeco.model.ComponentInstance;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;

public class InstanceRuntimeMetadataProvider extends RuntimeMetadataProvider {

	private static final long serialVersionUID = 1L;
	
	private Map<Class<?>, Component> parsedComponents;
	private List<Class<?>> parsedEnsembles;

	public InstanceRuntimeMetadataProvider() {
		this.parsedComponents = new HashMap<Class<?>, Component>();
		this.parsedEnsembles = new LinkedList<>();
	}

	public void fromComponentInstance(ComponentDefinition instance) {
		assert (instance != null);
		Class<?> clazz = instance.getClass();
		Component component = parsedComponents.get(clazz);
		if (component == null) {
			try {
				component = new Component(clazz.getName(), clazz.getName(),
						extractComponentProcesses(clazz));
				parsedComponents.put(clazz, component);
			} catch (ParseException pe) {
				Log.e("InstanceRuntimeMetadataProvider exception", pe);
				return;
			}
		}
		runtimeMetadata.addComponent(component);
		runtimeMetadata.addComponentInstance(new ComponentInstance(instance,
				component));
	}

	public void fromEnsembleDefinition(Class<?> ensembleDefinition) {
		assert (ensembleDefinition != null);
		if (!parsedEnsembles.contains(ensembleDefinition)) {
			try {
				runtimeMetadata
						.addEnsemble(extractEnsembleProcess(ensembleDefinition));
				parsedEnsembles.add(ensembleDefinition);

			} catch (ParseException pe) {
				Log.e("InstanceRuntimeMetadataProvider exception", pe);
				return;
			}
		}
	}

}
