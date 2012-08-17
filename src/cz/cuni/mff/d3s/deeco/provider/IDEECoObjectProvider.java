package cz.cuni.mff.d3s.deeco.provider;

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;

public interface IDEECoObjectProvider {

	public List<SchedulableProcess> getProcesses();
	
	public List<ComponentKnowledge> getKnowledges();	
}
