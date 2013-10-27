package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * This class allows the user to deal with KnowledgeSet from two perspectives:
 * 1- reading the values from the KnowledgeSet.
 * 2- associating the registers with their tirggerListeners.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */
import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

public interface ReadOnlyKnowledgeManager {

	public ValueSet get(Collection<KnowledgeReference> knowledgeReferenceList);
	public void register(Trigger trigger, TriggerListener triggerListener);
}
