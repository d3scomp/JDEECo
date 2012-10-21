package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableComponentProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;

public class ParsedComponent {

	private ComponentKnowledge initialKnowledge;
	private List<SchedulableComponentProcessCreator> processCreators;

	public ParsedComponent() {
		initialKnowledge = null;
		processCreators = new LinkedList<SchedulableComponentProcessCreator>();
	}

	public ParsedComponent(List<SchedulableComponentProcessCreator> processCreators, ComponentKnowledge initialKnowledge) {
		this.initialKnowledge = initialKnowledge;
		this.processCreators = processCreators;
	}

	public ComponentKnowledge getInitialKnowledge() {
		return initialKnowledge;
	}

	public List<SchedulableComponentProcessCreator> getProcesses() {
		return processCreators;
	}

}
