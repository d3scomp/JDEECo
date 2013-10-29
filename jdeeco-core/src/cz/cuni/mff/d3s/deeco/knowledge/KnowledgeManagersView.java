package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * This interface allows the user to get the KnowledgeManagers and register/unregister 
 * the triggers of the others.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */
import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.TriggerListener;


public interface KnowledgeManagersView {
	
	public Collection<ReadOnlyKnowledgeManager> getOthersKnowledgeManagers();
	// Rima : It is a little bit confusing to have the same name 
	//        register/unregister as in KnowledgeManager. 
	public void register(Trigger trigger, TriggerListener triggerListener);
	public void unregister(Trigger trigger, TriggerListener triggerListener);
	
}
