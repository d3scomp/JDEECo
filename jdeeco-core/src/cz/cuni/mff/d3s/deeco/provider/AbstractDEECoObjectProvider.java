package cz.cuni.mff.d3s.deeco.provider;

import java.io.Serializable;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableEnsembleProcessCreator;

/**
 * Class defining main functionalities of component and ensemble provider.
 * 
 * @author Michal Kit
 * 
 */
public abstract class AbstractDEECoObjectProvider implements Serializable {

	protected List<ParsedComponent> components;
	protected List<SchedulableEnsembleProcessCreator> ensembles;

	/**
	 * Retrieves all provided components.
	 * 
	 * @return Provided components.
	 */
	public List<ParsedComponent> getComponents() {
		if (components == null)
			processComponents();
		return components;
	}

	/**
	 * Retrieves all provided ensembles.
	 * 
	 * @return Provided ensembles.
	 */
	public List<SchedulableEnsembleProcessCreator> getEnsembles() {
		if (ensembles == null)
			processEnsembles();
		return ensembles;
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
	 * Processes components for provisioning.
	 */
	abstract protected void processComponents();

	/**
	 * Processes ensembles for provisioning.
	 */
	abstract protected void processEnsembles();

}
