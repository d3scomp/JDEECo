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

	public void register(Trigger trigger, ShadowsTriggerListener triggerListener);
	public void unregister(Trigger trigger, ShadowsTriggerListener triggerListener);
	
}
