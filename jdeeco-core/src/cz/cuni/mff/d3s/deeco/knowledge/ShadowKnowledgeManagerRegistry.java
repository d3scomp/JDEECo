package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

/**
 * 
 * This interface allows to get all existing shadow replicas and also allows to register/unregister the triggers 
 * and triggerListeners for each shadow replica even the future once.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */
public interface ShadowKnowledgeManagerRegistry {
	
	public Collection<ReadOnlyKnowledgeManager> getShadowKnowledgeManagers();

	/**
	 * Registers the specified trigger and its listener within this shadow knowledge
	 * manager registry.
	 * 
	 * @param {@link Trigger}
	 *            trigger to be listen for.
	 * @param {@link ShadowsTriggerListener}
	 *            listener to be notified in case of triggering event.
	 */
	public void register(Trigger trigger, ShadowsTriggerListener triggerListener);

	/**
	 * Unregisters the specified trigger and its listener from this shadow knowledge
	 * manager registry.
	 * 
	 * @param {@link Trigger}
	 *            trigger to be unregistered.
	 * @param {@link ShadowsTriggerListener}
	 *            listener to be unregistered.
	 */
	public void unregister(Trigger trigger, ShadowsTriggerListener triggerListener);
	
}
