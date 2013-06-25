package cz.cuni.mff.d3s.deeco.provider;

import java.io.Serializable;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;

/**
 * Class defining main functionalities of component and ensemble provider.
 * 
 * @author Michal Kit
 * 
 */
public abstract class AbstractDEECoObjectProvider implements Serializable {

	private static final long serialVersionUID = 1945721980050303806L;
	
	protected List<ParsedComponent> components;
	protected List<SchedulableEnsembleProcess> ensembles;

	// TODO: This should return ComponentInstances
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
	public List<SchedulableEnsembleProcess> getEnsembles() {
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
