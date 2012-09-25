package cz.cuni.mff.d3s.deeco.provider;

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

 
public abstract class AbstractDEECoObjectProvider {

	protected List<SchedulableProcess> processes;
	protected List<ComponentKnowledge> knowledges;
	protected KnowledgeManager km;

	public List<SchedulableProcess> getProcesses(KnowledgeManager km) {
		if (km != this.km) {
			this.km = km;
			processProcesses();
		}
		return processes;
	}

	public List<ComponentKnowledge> getKnowledges(KnowledgeManager km) {
		if (km != this.km || km == null) {
			this.km = km;
			processKnowledges();
		}
		return knowledges;
	}

	abstract protected void processKnowledges();

	abstract protected void processProcesses();

}
