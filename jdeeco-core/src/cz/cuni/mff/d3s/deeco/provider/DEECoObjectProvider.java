package cz.cuni.mff.d3s.deeco.provider;

import static cz.cuni.mff.d3s.deeco.processor.ComponentParser.extractComponentProcesses;
import static cz.cuni.mff.d3s.deeco.processor.ComponentParser.extractInitialKnowledge;
import static cz.cuni.mff.d3s.deeco.processor.EnsembleParser.extractEnsembleProcess;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;

/**
 * Class defining main functionalities of component and ensemble provider.
 * 
 * @author Michal Kit
 * 
 */
public class DEECoObjectProvider implements Serializable {

	private static final long serialVersionUID = 1945721980050303806L;

	protected List<Component> initialKnowledge;
	protected List<ComponentInstance> componentInstances;
	protected List<SchedulableEnsembleProcess> ensembles;

	public DEECoObjectProvider() {
		this.initialKnowledge = new LinkedList<>();
		this.componentInstances = new LinkedList<>();
		this.ensembles = new LinkedList<>();
	}

	public DEECoObjectProvider(List<Class<?>> components,
			List<Class<?>> ensembles) {
		this();
		addInitialKnowledgeFromDefinitions(components);
		addEnsembles(ensembles);
	}
	
	public DEECoObjectProvider(Component [] initialKnowledge, Class<?> [] ensembles) {
		this();
		addInitialKnowledge(Arrays.asList(initialKnowledge));
		addEnsembles(Arrays.asList(ensembles));
	}

	/**
	 * Retrieves initial knowledge of the components provided.
	 * 
	 * @return list of initial knowledge
	 */
	public List<Component> getInitialKnowledge() {
		return new LinkedList<>(initialKnowledge);
	}

	/**
	 * Retrieves initial knowledge for the particular component instance
	 * 
	 * @param componentInstance
	 *            component instance for which initial knowledge should be
	 *            returned
	 * @return initial knowledge of the component.
	 */
	public Component getInitialKnowledgeForComponentInstance(
			ComponentInstance componentInstance) {
		if (componentInstance != null) {
			int index = componentInstances.indexOf(componentInstance);
			if (index > -1)
				return initialKnowledge.get(index);
		}
		return null;
	}

	/**
	 * Retrieves all provided component instances.
	 * 
	 * @return Provided components.
	 */
	public List<ComponentInstance> getComponentInstances() {
		return new LinkedList<>(componentInstances);
	}

	/**
	 * Retrieves all provided ensembles.
	 * 
	 * @return Provided ensembles.
	 */
	public List<SchedulableEnsembleProcess> getEnsembles() {
		return new LinkedList<>(ensembles);
	}

	/**
	 * Returns specific context class loader for the classes returned by this
	 * Provider.
	 * 
	 * @return Context class Loader
	 */
	public ClassLoader getContextClassLoader() {
		return null;
	}

	/**
	 * Adds new initial knowledge of a component (given by its definition) to
	 * this provider.
	 * 
	 * @param component
	 *            a component for which initial knowledge should be added.
	 */
	public void addInitialKnowledgeFromDefinition(Class<?> component) {
		if (component != null) {
			Component ik = extractInitialKnowledge(component);
			addInitialKnowledge(ik);
		}
	}

	/**
	 * Adds new initial knowledge of components (given by their definitions) to
	 * this provider.
	 * 
	 * @param components
	 *            components for which initial knowledge should be added.
	 */
	public void addInitialKnowledgeFromDefinitions(List<Class<?>> components) {
		if (components != null) {
			for (Class<?> clazz : components) {
				addInitialKnowledgeFromDefinition(clazz);
			}
		}
	}

	/**
	 * Adds new initial knowledge of a component to this provider.
	 * 
	 * @param initialKnowledge
	 *            knowledge to be added
	 */
	public void addInitialKnowledge(Component initialKnowledge) {
		if (initialKnowledge != null) {
			componentInstances.add(new ComponentInstance(
					extractComponentProcesses(initialKnowledge.getClass(),
							initialKnowledge.id)));
			this.initialKnowledge.add(initialKnowledge);
		}
	}

	/**
	 * Adds new initial knowledge of the components to this provider.
	 * 
	 * @param initialKnowledge
	 *            knowledge to be added
	 */
	public void addInitialKnowledge(List<Component> initialKnowledge) {
		if (initialKnowledge != null) {
			for (Component ik : initialKnowledge) {
				addInitialKnowledge(ik);
			}
		}
	}

	/**
	 * Adds a new ensemble definition to this provider.
	 * 
	 * @param ensemble
	 *            an ensemble definition to be added.
	 */
	public void addEnsemble(Class<?> ensemble) {
		if (ensemble != null) {
			try {
				this.ensembles.add(extractEnsembleProcess(ensemble));
			} catch (ParseException pe) {
				Log.e(pe.getMessage());
			}
		}
	}

	/**
	 * Adds new ensemble definitions to this provider.
	 * 
	 * @param ensembles
	 *            ensemble definitions to be added.
	 */
	public void addEnsembles(List<Class<?>> ensembles) {
		if (ensembles != null) {
			for (Class<?> ec : ensembles) {
				addEnsemble(ec);
			}
		}
	}
}
