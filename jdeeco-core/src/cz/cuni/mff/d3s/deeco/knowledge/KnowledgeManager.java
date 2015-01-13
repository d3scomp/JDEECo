package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;

/**
 * This interface allows the user to add, update and read the values from
 * KnowledgeSet. Also, it allows to bind a trigger to triggerListener or unbind
 * it.
 * 
 * It is assumed that {@link KnowledgePath} instances used with this instance
 * are absolute, meaning that they are fully evaluated.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * 
 */

public interface KnowledgeManager extends ReadOnlyKnowledgeManager {

	/**
	 * Updates this knowledge manager with the given {@link ChangeSet} values.
	 * 
	 * @param changeSet
	 *            update changes.
	 * @throws KnowledgeUpdateException
	 *             thrown when knowledge could not have been updated
	 */
	void update(ChangeSet changeSet) throws KnowledgeUpdateException;
	void markAsLocal(Collection<KnowledgePath> knowledgePaths);
	void addSecurityTags(KnowledgePath knowledgePath, Collection<KnowledgeSecurityTag> newSecurityTags);	
}
