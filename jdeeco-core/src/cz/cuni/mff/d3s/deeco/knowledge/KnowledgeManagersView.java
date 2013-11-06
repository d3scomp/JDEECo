package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

// FIXME TB: Any idea of some better name of the class? I would say this name is rather misleading.

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
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface KnowledgeManagersView {
	
	public Collection<ReadOnlyKnowledgeManager> getOthersKnowledgeManagers();
	// Rima : It is a little bit confusing to have the same name 
	//        register/unregister as in KnowledgeManager. 
	// TB@RA: Good point. However, I can't come up with any better name. Any suggestions?
	
	// FIXME TB: It would make more sense to have a separate listener for triggers on shadows since that listener
	// should additionally provide the information, in which shadow KM the trigger occured.
	public void register(Trigger trigger, ShadowsTriggerListener triggerListener);
	public void unregister(Trigger trigger, ShadowsTriggerListener triggerListener);
	
}
