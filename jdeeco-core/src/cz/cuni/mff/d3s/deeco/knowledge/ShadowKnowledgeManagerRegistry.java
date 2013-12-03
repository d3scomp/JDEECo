package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

/**
*
* This interface allows to get all existing local and replica knowledge managers excluding the caller knowledge manager, and it allows also to register/unregister 
* the triggers and shadow trigger listeners from "all"//"or shadow???"// knowledge managers in the shadow knowledge manager registry.
*
* @author Rima Al Ali <alali@d3s.mff.cuni.cz>
*
*/
public interface ShadowKnowledgeManagerRegistry {
	
	public Collection<ReadOnlyKnowledgeManager> getShadowKnowledgeManagers();

    /**
     * Registers the specified trigger and shadow trigger listener with its (shadow) knowledge
     * manager to Shadow Knowledge Manager Registry if they already are not exist.
     *
     * @param {@link Trigger}
     * 			trigger to be listened by specified ShadowTriggerListener for shadow knowledge manager.
     * @param {@link ShadowsTriggerListener}
     * 			listener to be notified in case of triggering event on any shadow knowledge manager.
     */
	public void register(Trigger trigger, ShadowsTriggerListener triggerListener);

    /**
     * Unregisters the specified shadow trigger listener, trigger and its (shadow) knowledge
     * manager from Shadow Knowledge Manager Registry.
     *
     * @param {@link Trigger}
     * 			trigger to be listened by specified ShadowTriggerListener for shadow knowledge manager.
     * @param {@link ShadowsTriggerListener}
     * 			listener to be notified in case of triggering event on any shadow knowledge manager.
     */
	public void unregister(Trigger trigger, ShadowsTriggerListener triggerListener);
	
}
