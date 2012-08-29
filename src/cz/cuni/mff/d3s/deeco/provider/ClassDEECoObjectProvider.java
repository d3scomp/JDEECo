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

	private Class<?> [] rawComponents;
	private Class<?> [] rawEnsembles;
	
	private ComponentParser cp;
	private EnsembleParser ep;
	
	
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
