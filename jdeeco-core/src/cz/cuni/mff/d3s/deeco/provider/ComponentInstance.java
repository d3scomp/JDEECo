package cz.cuni.mff.d3s.deeco.provider;

import java.io.Serializable;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;

public class ComponentInstance implements Serializable {
	private List<SchedulableComponentProcess> processes;
	
	public ComponentInstance(List<SchedulableComponentProcess> processes) {
		this.processes = processes;
	}
	
	public List<SchedulableComponentProcess> getProcesses() {
		return processes;
	}
}
