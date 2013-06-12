package cz.cuni.mff.d3s.deeco.runtime;

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;

/**
 * Interface declaring functionality delivering information about available
 * components and ensembles.
 * 
 * @author Michal Kit
 * 
 */
public interface IEnsembleComponentInformer {

	/**
	 * Retrieves the IDs of available components.
	 * 
	 * @return IDs of all available components.
	 */
	public List<String> getComponentsIds();

	/**
	 * Retrieves current knowledge state for the specified component.
	 * 
	 * @param componentId
	 *            component ID
	 * @return current component knowledge.
	 */
	public Object getComponentKnowledge(String componentId);

	/**
	 * Retrieves all available processes for the specified component.
	 * 
	 * @param componentId
	 *            component ID
	 * @return list of all available processes for the component
	 */
	public List<SchedulableComponentProcess> getComponentProcesses(
			String componentId);

	/**
	 * Retrieves all available ensembles definitions.
	 * 
	 * @return list of all available ensemble processes.
	 */
	public List<SchedulableEnsembleProcess> getEnsembleProcesses();
}
