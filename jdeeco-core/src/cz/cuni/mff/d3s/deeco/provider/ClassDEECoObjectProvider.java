package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.creators.IScheduleableProcessCreator;
import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.processor.ComponentParser;
import cz.cuni.mff.d3s.deeco.processor.EnsembleParser;

public class ClassDEECoObjectProvider extends AbstractDEECoObjectProvider {

	protected final List<Class<?>> rawComponents;
	protected final List<Class<?>> rawEnsembles;

	protected final List<SchedulableProcessCreator> processesCreators;
	
	protected final ComponentParser cp;
	protected final EnsembleParser ep;

	public ClassDEECoObjectProvider() {
		this(new LinkedList<Class<?>>(), new LinkedList<Class<?>>());
	}
	
	public ClassDEECoObjectProvider(List<Class<?>> components,
			List<Class<?>>  ensembles) {
		rawComponents = components;
		rawEnsembles = ensembles;
		cp = new ComponentParser();
		ep = new EnsembleParser();
		
		processesCreators = new LinkedList<SchedulableProcessCreator>();
	}
	
	public boolean addDEECoObjectClass(Class<?> clazz) {
		if (cp.isComponentDefinition(clazz))
			rawComponents.add(clazz);
		else if (ep.isEnsembleDefinition(clazz))
			rawEnsembles.add(clazz);
		else
			return false;
		return true;
	}
	
	public List<SchedulableProcessCreator> getProcessCreators() {
		return processesCreators;
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
		processesCreators.clear();

		for (Class<?> c : rawComponents) {
			ck = cp.extractInitialKnowledge(c);
			if (ck != null) {
				knowledges.add(ck);
				processesCreators.addAll(cp.extractComponentProcess(c, ck.id));
			}
		}
		for (Class<?> c : rawEnsembles) {
			processesCreators.add(ep.extractEnsembleProcess(c));
		}

		// Create processes from creators and bound them with knowledge
		// repository
		for (IScheduleableProcessCreator spc : processesCreators) {
			processes.add(spc.extract(km));
		}
	}

}
