package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

/**
 * 
 * This container interface allows to keep with local knowledge managers. It gives the ability to implement the creation of new local knowledge manager 
 * and add it to the container, and to define how to retrieve all available local knowledge managers in this container.  
 * In addition, it specify a method to register a new local listener to listen for events caused by local knowledge managers changes (i.e. add/remove local knowledge manager). 
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface LocalKnowledgeManagerContainer {

	/**
	 * Creates a new instance of local knowledge manager with the specified id, add it to the container and register all existing local listener to it.
	 * 
	 * @param String
	 * 			the identifier of the knowledge manager
	 * @return {@link KnowledgeManager} 
	 * 			the newly created object containing values for the specified knowledge paths.
	 */
	public KnowledgeManager createLocal(String id);

	/**
	 * Removes a local knowledge manager from the container and return it. This implies also informing the local listener about removing this knowledge manager. 
	 * 
	 * @param {@link KnowledgeManager}
	 * 				local knowledge manager to be removed 
	 * @return {@link KnowledgeManager} 
	 * 				the removed local knowledge manager object containing values for the specified knowledge paths
	 */
	public KnowledgeManager removeLocal(KnowledgeManager km);

	/**
	 * Retrieves all the local knowledge managers in the container.
	 * 
	 * @return List<{@link KnowledgeManager}> 
	 * 				object containing values for the specified knowledge paths
	 */
	public List<KnowledgeManager> getLocals();

	/**
	 * Adds the local listener to the container
	 * 
	 * @param {@link LocalListener}
	 * 				listens for adding local knowledge managers to the container or removing them.
	 */
	public void registerLocalListener(LocalListener listener);
}
