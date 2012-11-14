package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableEnsembleProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.processor.ComponentParser;
import cz.cuni.mff.d3s.deeco.processor.EnsembleParser;

/**
 * Provider class dealing with component and ensemble definitions retrieved from
 * Class definitions.
 * 
 * @author Michal Kit
 * 
 */
public class ClassDEECoObjectProvider extends AbstractDEECoObjectProvider {

	protected final List<Class<?>> rawComponents;
	protected final List<Class<?>> rawEnsembles;

	protected final ComponentParser cp;
	protected final EnsembleParser ep;

	public ClassDEECoObjectProvider() {
		this(new LinkedList<Class<?>>(), new LinkedList<Class<?>>());
	}

	public ClassDEECoObjectProvider(List<Class<?>> components,
			List<Class<?>> ensembles) {
		rawComponents = components;
		rawEnsembles = ensembles;
		cp = new ComponentParser();
		ep = new EnsembleParser();
	}

	/**
	 * Adds either compontent or ensemble definition class to its internal
	 * collection.
	 * 
	 * @param clazz
	 *            Class to be added.
	 * @return Addition result.
	 */
	public boolean addDEECoObjectClass(Class<?> clazz) {
		if (cp.isComponentDefinition(clazz))
			rawComponents.add(clazz);
		else if (ep.isEnsembleDefinition(clazz))
			rawEnsembles.add(clazz);
		else
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider#processComponents
	 * ()
	 */
	@Override
	protected synchronized void processComponents() {
		components = new LinkedList<ParsedComponent>();
		for (Class<?> c : rawComponents) {
			List<ComponentKnowledge> cks = cp.extractInitialKnowledge(c);
			if (cks != null) {
				for (ComponentKnowledge ck : cks)
					components.add(new ParsedComponent(cp.extractComponentProcess(
							c, ck.id), ck));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider#processEnsembles
	 * ()
	 */
	@Override
	protected synchronized void processEnsembles() {
		ensembles = new LinkedList<SchedulableEnsembleProcessCreator>();
		for (Class<?> c : rawEnsembles) {
			ensembles.add(ep.extractEnsembleProcess(c));
		}
	}

}
