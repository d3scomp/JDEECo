package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.LocalKnowledgeTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityTag;

/**
 * This interface allows the user to add, update and read the values from
 * KnowledgeSet. Also, it allows to bind a trigger to triggerListener or unbind
 * it.
 * 
 * It is assumed that {@link KnowledgePath} instances used with this instance
 * are absolute, meaning that they are fully evaluated.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * @author Ondřej Štumpf
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
	
	/**
	 * Updates this knowledge manager with the given {@link ChangeSet} values.
	 * 
	 * @param changeSet
	 *            update changes.
	 * @param shadowComponentId
	 * 			  the ID of the component which owns the change set	           
	 * @throws KnowledgeUpdateException
	 *             thrown when knowledge could not have been updated
	 */
	void update(ChangeSet changeSet, String shadowComponentId) throws KnowledgeUpdateException;
	
	/**
	 * Marks the knowledge paths as local and assigns them a {@link LocalKnowledgeTag}.
	 *
	 * @param knowledgePaths
	 *            the knowledge paths
	 */
	void markAsLocal(Collection<KnowledgePath> knowledgePaths);
	
	
	/**
	 * Adds the security tag to the given knowledge path. Each security tag corresponds either to {@link Allow} annotation,
	 * or is an instance of {@link LocalKnowledgeTag} for local knowledge.
	 *
	 * @param knowledgePath
	 *            the knowledge path
	 * @param newSecurityTag
	 *            the new security tag
	 */
	void addSecurityTag(KnowledgePath knowledgePath, SecurityTag newSecurityTag);
	
	/**
	 * Sets the security tags for the given knowledge path (i.e. removes any previous).
	 *
	 * @param knowledgePath
	 *            the knowledge path
	 * @param newSecurityTags
	 *            the new security tags
	 */
	void setSecurityTags(KnowledgePath knowledgePath, Collection<SecurityTag> newSecurityTags);
	
	/**
	 * Locks the knowledge path, which signals that it is being used as an parameter for a {@link SecurityRole} and cannot be changed.
	 *
	 * @param knowledgePath
	 *            the knowledge path
	 */
	void lockKnowledgePath(KnowledgePath knowledgePath);

	boolean isOfSystemComponent();
	
	Map<KnowledgeChangeTrigger, List<TriggerListener>> getKnowledgeChangeListeners();
}
