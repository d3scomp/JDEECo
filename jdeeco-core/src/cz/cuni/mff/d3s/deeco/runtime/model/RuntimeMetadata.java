package cz.cuni.mff.d3s.deeco.runtime.model;

import java.util.LinkedList;
import java.util.List;

public class RuntimeMetadata {

	private final List<Component> components;
	private final List<ComponentInstance> componentInstances;
	private final List<Ensemble> ensembles;

	public RuntimeMetadata() {
		this(new LinkedList<Component>(), new LinkedList<ComponentInstance>(),
				new LinkedList<Ensemble>());
	}

	public RuntimeMetadata(List<Component> components,
			List<ComponentInstance> componentInstances, List<Ensemble> ensembles) {
		super();
		this.components = components;
		this.componentInstances = componentInstances;
		this.ensembles = ensembles;
	}

	public List<Component> getComponents() {
		return components;
	}

	public List<ComponentInstance> getComponentInstances() {
		return componentInstances;
	}

	public List<Ensemble> getEnsembles() {
		return ensembles;
	}

	public void addComponent(Component component) {
		components.add(component);
	}

	public void addComponentInstance(ComponentInstance componentInstance) {
		componentInstances.add(componentInstance);
	}
	
	public void addEnsemble(Ensemble ensemble) {
		ensembles.add(ensemble);
	}
}
