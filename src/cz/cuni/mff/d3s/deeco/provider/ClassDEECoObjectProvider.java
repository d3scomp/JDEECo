package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.processor.ComponentParser;
import cz.cuni.mff.d3s.deeco.processor.EnsembleParser;


public class ClassDEECoObjectProvider extends AbstractDEECoObjectProvider {

	protected final List<Class<?>> rawComponents = new LinkedList<Class<?>>();
	protected final List<Class<?>> rawEnsembles = new LinkedList<Class<?>>();;
	
	protected final ComponentParser cp = new ComponentParser();
	protected final EnsembleParser ep = new EnsembleParser();
	
	public ClassDEECoObjectProvider() {}
	
	public ClassDEECoObjectProvider(List<Class<?>> components, List<Class<?>> ensembles) {
		super();
		if (components != null)
			rawComponents.addAll(components);
		if (ensembles != null)
			rawEnsembles.addAll(ensembles);
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
		ComponentKnowledge ck;
		processes = new LinkedList<SchedulableProcess>();
		knowledges = new LinkedList<ComponentKnowledge>();
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
