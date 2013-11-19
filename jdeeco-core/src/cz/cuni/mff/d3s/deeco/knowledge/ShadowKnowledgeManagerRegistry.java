package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

// FIXME TB: Rewrite the Javadoc comment to the class. At this point it's a bit misleading. In particular:
// - "user" ???
// - "triggers of the others" ???
// - It should clearly say that this allows obtaining the shadow replicas and that it allows registering for
//   changes in all shadow replicas (even in the future ones)

/**
 * This interface allows the user to get the KnowledgeManagers and register/unregister 
 * the triggers of the others.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */
public interface ShadowKnowledgeManagerRegistry {
	
	public Collection<ReadOnlyKnowledgeManager> getShadowKnowledgeManagers();

	public void register(Trigger trigger, ShadowsTriggerListener triggerListener);
	public void unregister(Trigger trigger, ShadowsTriggerListener triggerListener);
	
}
