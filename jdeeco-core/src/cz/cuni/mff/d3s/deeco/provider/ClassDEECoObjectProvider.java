package cz.cuni.mff.d3s.deeco.provider;

import static cz.cuni.mff.d3s.deeco.processor.ComponentParser.extractComponentProcess;
import static cz.cuni.mff.d3s.deeco.processor.ComponentParser.extractInitialKnowledge;
import static cz.cuni.mff.d3s.deeco.processor.ComponentParser.isComponentDefinition;
import static cz.cuni.mff.d3s.deeco.processor.EnsembleParser.extractEnsembleProcess;
import static cz.cuni.mff.d3s.deeco.processor.EnsembleParser.isEnsembleDefinition;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableEnsembleProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;

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

	public ClassDEECoObjectProvider() {
		this(new LinkedList<Class<?>>(), new LinkedList<Class<?>>());
	}

	public ClassDEECoObjectProvider(List<Class<?>> components,
			List<Class<?>> ensembles) {
		rawComponents = components;
		rawEnsembles = ensembles;
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
		if (isComponentDefinition(clazz))
			rawComponents.add(clazz);
		else if (isEnsembleDefinition(clazz))
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
			List<ComponentKnowledge> cks = extractInitialKnowledge(c);
			if (cks != null) {
				for (ComponentKnowledge ck : cks)
					components.add(new ParsedComponent(extractComponentProcess(
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
			ensembles.add(extractEnsembleProcess(c));
		}
	}

}
