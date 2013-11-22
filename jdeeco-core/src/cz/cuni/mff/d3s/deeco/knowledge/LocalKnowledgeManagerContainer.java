package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

/**
 * 
 * Allows adding/removing local knowledge manager to a container with ability to register a listener for each new local knowledge manager. Also, it allows to 
 * return all the available local knowledge managers in the container. 
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface LocalKnowledgeManagerContainer {

	/**
	 * A factory for a local knowledge manager.
	 * 
	 * @param String
	 * @return {@link KnowledgeManager} 
	 * 				object containing values for the specified knowledge paths.
	 */
	public KnowledgeManager createLocal(String id);

	/**
	 * Removes a local knowledge manager from the container and return it.
	 * 
	 * @param {@link KnowledgeManager}
	 * @return {@link KnowledgeManager} 
	 * 				object containing values for the specified knowledge paths
	 */
	public KnowledgeManager removeLocal(KnowledgeManager km);

	/**
	 * Retrieves all local knowledge managers in the container.
	 * 
	 * @return List<{@link KnowledgeManager}> 
	 * 				object containing values for the specified knowledge paths
	 */
	public List<KnowledgeManager> getLocals();

	/**
	 * Retrieves values for the collection of the {@link KnowledgePath} objects.
	 * 
	 * @param {@link ReplicaListener}
	 * 				listens for adding replicas to the container or removing them from it.
	 */
	public void registerLocalListener(LocalListener listener);
}
