package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.processor.ComponentParser;
import cz.cuni.mff.d3s.deeco.processor.EnsembleParser;


public class ClassDEECoObjectProvider extends AbstractDEECoObjectProvider {

	private Class<?> [] rawComponents;
	private Class<?> [] rawEnsembles;
	
	private ComponentParser cp;
	private EnsembleParser ep;
	
	
	public ClassDEECoObjectProvider(Class<?> [] components, Class<?> [] ensembles) {
		rawComponents = components;
		rawEnsembles = ensembles;
		cp = new ComponentParser();
		ep = new EnsembleParser();
	}


	@Override
	protected synchronized void processKnowledges() {
		processAll();
	}


	@Override
	protected synchronized void processProcesses() {
		processAll();
	}
	
	private void processAll() {
		knowledges = new LinkedList<ComponentKnowledge>();
		processes = new LinkedList<SchedulableProcess>();
		ComponentKnowledge ck;
		for (Class<?> c : rawComponents) {
			ck = cp.extractInitialKnowledge(c);
			if (ck != null) {
				knowledges.add(ck);
				processes.addAll(cp.extractComponentProcess(c, ck.id));
			}
		}
		for (Class<?> c : rawEnsembles) {
			processes.add(ep.extractEnsembleProcess(c));
		}
	}

}
