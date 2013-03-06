package cz.cuni.mff.d3s.deeco.provider;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;

/**
 * jDEECo internal representation of a component, containing both initial
 * knowledge and component process crreators.
 * 
 * @author Michal Kit
 * 
 */
public class ParsedComponent implements Serializable {

	private static final long serialVersionUID = 7598262003638318904L;
	
	private ComponentKnowledge initialKnowledge;
	private List<SchedulableComponentProcess> processCreators;

	public ParsedComponent() {
		initialKnowledge = null;
		processCreators = new LinkedList<SchedulableComponentProcess>();
	}

	public ParsedComponent(
			List<SchedulableComponentProcess> processCreators,
			ComponentKnowledge initialKnowledge) {
		this.initialKnowledge = initialKnowledge;
		this.processCreators = processCreators;
	}

	/**
	 * Retrieves initial knowledge of the component.
	 * 
	 * @return Initial knowledge of the component.
	 */
	public ComponentKnowledge getInitialKnowledge() {
		return initialKnowledge;
	}

	/**
	 * Retrieves component processes.
	 * 
	 * @return Processes of the component.
	 */
	public List<SchedulableComponentProcess> getProcesses() {
		return processCreators;
	}

}
