package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

/**
 * 
 * This container interface allows to keep replica knowledge managers. It gives the ability to implement the creation of new replica knowledge manager 
 * and add it to the container, and to define how to retrieve all available replica knowledge managers in this container.  
 * In addition, it specify a method to register a new replica listener to listen for events caused by replica knowledge managers changes (i.e. add/remove replica knowledge manager). .
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface ReplicaKnowledgeManagerContainer {
	
	/**
	 * Creates a new instance of replica knowledge manager with the specified id, add it to the container and register all existing replica listener to it.
	 * 
	 * @param String
	 * 			the identifier of the knowledge manager
	 * @return {@link KnowledgeManager} 
	 * 			the newly created object containing values for the specified knowledge paths.
	 */
	public KnowledgeManager createReplica(String id);
	
	/**
	 * Removes a replica knowledge manager from the container and return it. This implies also informing the replica listener about removing this knowledge manager. 
	 * 
	 * @param {@link KnowledgeManager}
	 * 				replica knowledge manager to be removed 
	 * @return {@link KnowledgeManager} 
	 * 				the removed replica knowledge manager object containing values for the specified knowledge paths
	 */
	public KnowledgeManager removeReplica(KnowledgeManager km);
	
	/**
	 * Retrieves all the replica knowledge managers in the container.
	 * 
	 * @return List<{@link KnowledgeManager}> 
	 * 				object containing values for the specified knowledge paths
	 */
	public List<KnowledgeManager> getReplicas();
	
	/**
	 * Adds the replica listener to the container
	 * 
	 * @param {@link ReplicaListener}
	 * 				listens for adding replica knowledge managers to the container or removing them.
	 */
	public void registerReplicaListener(ReplicaListener listener);
}
