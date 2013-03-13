package cz.cuni.mff.d3s.deeco.provider;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.knowledge.Component;

/**
 * jDEECo internal representation of a component, containing both initial
 * knowledge and component process crreators.
 * 
 * @author Michal Kit
 * 
 */
public class ParsedComponent implements Serializable {

	private static final long serialVersionUID = 7598262003638318904L;
	
	private Component initialKnowledge;
	private List<SchedulableComponentProcess> processes;

	public ParsedComponent() {
		initialKnowledge = null;
		processes = new LinkedList<SchedulableComponentProcess>();
	}

	public ParsedComponent(
			List<SchedulableComponentProcess> processCreators,
			Component initialKnowledge) {
		this.initialKnowledge = initialKnowledge;
		this.processes = processCreators;
	}

	/**
	 * Retrieves initial knowledge of the component.
	 * 
	 * @return Initial knowledge of the component.
	 */
	public Component getInitialKnowledge() {
		return initialKnowledge;
	}

	/**
	 * Retrieves component processes.
	 * 
	 * @return Processes of the component.
	 */
	public List<SchedulableComponentProcess> getProcesses() {
		return processes;
	}

}
