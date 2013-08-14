package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.provider.DEECoObjectProvider;

/**
 * Interface declaring jDEECo runtime functionality.
 * 
 * @author Michal Kit
 * 
 */
public interface IRuntime extends IEnsembleComponentInformer {

	/**
	 * Sets scheduler that will be used within the runtime. Used by the OSGi
	 * framework.
	 * 
	 * @param scheduler
	 *            reference to the scheduler instance that needs to be used
	 *            within this runtime.
	 */
	public void setScheduler(Object scheduler);

	/**
	 * Unsets the scheduler that might be used within this runtime. Used by the
	 * OSGi framework.
	 * 
	 * @param scheduler
	 *            reference to the scheduler instacne that needs to be unset.
	 */
	public void unsetScheduler(Object scheduler);

	/**
	 * Sets the knowledge manager that is supposed to be used within this
	 * runtime. Used by the OSGi framework.
	 * 
	 * @param km
	 *            knowledge manager instance that needs to be set.
	 */
	public void setKnowledgeManager(Object km);

	/**
	 * Unsets the knowledge manager that might be used within this runtime. Used
	 * by the OSGi framework.
	 * 
	 * @param km
	 *            knowledge manager instance that needs to be unset.
	 */
	public void unsetKnowledgeManager(Object km);

	/**
	 * Registers components and ensembles to the runtime.
	 * 
	 * @param provider
	 *            provider instatnce that delivers components and ensembles
	 *            definitions.
	 */
	public void registerComponentsAndEnsembles(
			DEECoObjectProvider provider);
	
	/**
	 * Returns current time.
	 * 
	 * @return
	 */
	public long getTime();

	/**
	 * Checks whether the runtime is currently scheduling registered processes.
	 * 
	 * @return
	 */
	public boolean isRunning();

	/**
	 * Starts both component and ensemble processes scheduled execution.
	 */
	public void startRuntime();

	/**
	 * Stops both component and ensemble processes scheduled execution.
	 */
	public void stopRuntime();

}
