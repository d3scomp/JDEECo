package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableEnsembleProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.processor.ComponentParser;
import cz.cuni.mff.d3s.deeco.processor.EnsembleParser;

public class ClassDEECoObjectProvider extends AbstractDEECoObjectProvider {

	protected final List<Class<?>> rawComponents;
	protected final List<Class<?>> rawEnsembles;
	
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

	@Override
	protected synchronized void processComponents() {
		ComponentKnowledge ck;
		components = new LinkedList<ParsedComponent>();
		for (Class<?> c : rawComponents) {
			ck = cp.extractInitialKnowledge(c);
			if (ck != null) {
				components.add(new ParsedComponent(cp.extractComponentProcess(c, ck.id), ck));
			}
		}
	}

	@Override
	protected synchronized void processEnsembles() {
		ensembles = new LinkedList<SchedulableEnsembleProcessCreator>();
		for (Class<?> c : rawEnsembles) {
			ensembles.add(ep.extractEnsembleProcess(c));
		}
	}

}
