package cz.cuni.mff.d3s.deeco.processor;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;

public abstract class ProcessedHolder {
	protected final List<Object> processes;
	protected final List<ComponentKnowledge> knowledges;
	
	protected final ComponentParser cp;
	protected final EnsembleParser ep;
	
	public ProcessedHolder() {
		processes = new LinkedList<Object>();
		knowledges = new LinkedList<ComponentKnowledge>();
		cp = new ComponentParser();
		ep = new EnsembleParser();
	}
	
	public void parseClass(Class<?> clazz) {
		if (cp.isComponentDefinition(clazz)) {
			ComponentKnowledge initialKnowledge = cp
					.extractInitialKnowledge(clazz);
			if (initialKnowledge != null) {
				extractComponentProcess(clazz,
						initialKnowledge.id);
				knowledges.add(initialKnowledge);
			}
		} else if (ep.isEnsembleDefinition(clazz)) {
			extractEnsembleProcess(clazz);
		}
	}
	
	public List<Object> getProcesses() {
		return processes;
	}

	public List<ComponentKnowledge> getKnowledges() {
		return knowledges;
	}

	protected abstract void extractComponentProcess(Class<?> clazz, String id);
	protected abstract void extractEnsembleProcess(Class<?> clazz);
}
