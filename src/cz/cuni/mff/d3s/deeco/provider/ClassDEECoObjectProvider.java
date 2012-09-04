package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.creators.IScheduleableProcessCreator;
import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
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
	public ClassDEECoObjectProvider(KnowledgeManager km, Class<?> [] components, Class<?> [] ensembles) {
		super(km);
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
		ComponentKnowledge ck;
		processes = new LinkedList<SchedulableProcess>();
		knowledges = new LinkedList<ComponentKnowledge>();
		processes = new LinkedList<SchedulableProcess>();
		
		List<SchedulableProcessCreator> processesCreators = new LinkedList<SchedulableProcessCreator>();
		
		for (Class<?> c : rawComponents) {
			ComponentKnowledge ck = cp.extractInitialKnowledge(c);
			if (ck != null) {
				knowledges.add(ck);
				processesCreators.addAll(cp.extractComponentProcess(c, ck.id));
			}
		}
		for (Class<?> c : rawEnsembles) {
			processesCreators.add(ep.extractEnsembleProcess(c));
		}
		
		// Create processes from creators and bound them with knowledge repository
		for(IScheduleableProcessCreator spc : processesCreators) {
			processes.add( spc.extract(km));
		}
	}

}
