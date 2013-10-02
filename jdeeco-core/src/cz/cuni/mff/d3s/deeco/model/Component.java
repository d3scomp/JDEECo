package cz.cuni.mff.d3s.deeco.model;

import java.util.List;

public class Component {
	private String id;
	private String name;
	private List<ComponentProcess> processes;
	
	public Component(String id, String name, List<ComponentProcess> processes) {
		super();
		this.id = id;
		this.name = name;
		this.processes = processes;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<ComponentProcess> getProcesses() {
		return processes;
	}
}
