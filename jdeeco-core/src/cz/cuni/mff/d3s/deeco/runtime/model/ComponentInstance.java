package cz.cuni.mff.d3s.deeco.runtime.model;

import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;

public class ComponentInstance {
	private String id;
	private ComponentDefinition initialKnowledge;
	private Component component;

	public ComponentInstance(ComponentDefinition initialKnowledge,
			Component component) {
		this(initialKnowledge.id, initialKnowledge, component);
	}

	public ComponentInstance(String id, ComponentDefinition initialKnowledge,
			Component component) {
		super();
		this.id = id;
		this.initialKnowledge = initialKnowledge;
		this.component = component;
	}

	public String getId() {
		return id;
	}

	public ComponentDefinition getInitialKnowledge() {
		return initialKnowledge;
	}

	public Component getComponent() {
		return component;
	}

}
