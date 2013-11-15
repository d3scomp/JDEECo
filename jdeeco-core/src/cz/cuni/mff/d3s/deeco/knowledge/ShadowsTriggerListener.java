/**
 * 
 */
package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

/**
 * Gets called by a shadow knowledge manager registry when an event in the knowledge of one of the shadows matches the registered trigger.
 * 
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public interface ShadowsTriggerListener {
	public void triggered(ReadOnlyKnowledgeManager knowledgeManager, Trigger trigger);
}
