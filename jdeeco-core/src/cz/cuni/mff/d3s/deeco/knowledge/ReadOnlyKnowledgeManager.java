package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

/**
 * This interface allows to read the value of a determined knowledgeList. Also, the interface allows 
 * to register/unregister a trigger and a triggerListener. 
 * 
 * It is assumed that {@link KnowledgePath} instances used with this instance are
 * absolute, meaning that they are fully evaluated.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * @author Ondřej Štumpf
 * 
 */
public interface ReadOnlyKnowledgeManager {

	/**
	 * Retrieves values for the collection of the {@link KnowledgePath} objects.
	 * 
	 * @param knowledgeReferenceList
	 * @return {@link ValueSet} object containing values for the specified
	 *         knowledge paths
	 * @throws KnowledgeNotExistentException
	 *             when there is no value for at least one knowledge path
	 */
	public ValueSet get(Collection<KnowledgePath> knowledgeReferenceList)
			throws KnowledgeNotFoundException;

	/**
	 * Registers the specified trigger and its listener within this knowledge
	 * manager.
	 * 
	 * @param trigger
	 *            trigger to be listened for.
	 * @param triggerListener
	 *            listener to be notified in case of triggering event.
	 */
	public void register(Trigger trigger, TriggerListener triggerListener);

	/**
	 * Unregisters the specified trigger and its listener from this knowledge
	 * manager.
	 * 
	 * @param trigger
	 *            trigger to be unregistered.
	 * @param triggerListener
	 *            listener to be unregistered.
	 */
	public void unregister(Trigger trigger, TriggerListener triggerListener);
	
	/**
	 * Retrieves the id of this instance.
	 * 
	 * @return id
	 */
	public String getId();
	
	/**
	 * Checks if the knowledge path has been decorated with the {@link Local} annotation.
	 * @param knowledgePath
	 * @return true if the knowledge is local
	 */
	public boolean isLocal(KnowledgePath knowledgePath);
	
	/**
	 * Gets the knowledge paths that were decorated with the {@link Local} annotation
	 * @return the list of knowledge paths
	 */
	public Collection<KnowledgePath> getLocalPaths();
	
	/**
	 * Local knowledge managers are associated with their local components; replica knowledge managers
	 * are associated with such local component that was used to decrypt the knowledge.
	 * @return component
	 */
	public ComponentInstance getComponent();

	/**
	 * Gets security annotations associated with given field (setup during annotation processing)
	 * @param pathNodeField
	 * @return list of knowledge security tags (without local tags)
	 */
	List<KnowledgeSecurityTag> getKnowledgeSecurityTags(PathNodeField pathNodeField);
	
	/**
	 * Gets security annotations associated with given field (setup during annotation processing)
	 * @param pathNodeField
	 * @return list of security tags (including local tags)
	 */
	List<SecurityTag> getSecurityTags(PathNodeField pathNodeField);
	
	/**
	 * Returns the ID of the component from which this knowledge path comes.
	 * @param knowledgePath
	 * @return the ID of the component
	 */
	String getAuthor(KnowledgePath knowledgePath);
	
	/**
	 * Returns true if given knowledge path is a parameter of some security role and therefore its value cannot be modified
	 * @param knowledgePath
	 * @return true if the knowledge value cannot be modified
	 */
	boolean isLocked(KnowledgePath knowledgePath);
	
}
