package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

/**
 * 
 * Allows adding/removing replica knowledge manager to a container with ability to register a listener for each new replica knowledge manager. Also, it allows to 
 * return all the available replicas in the container. 
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface ReplicaKnowledgeManagerContainer {
	
	/**
	 * A factory for a replica knowledge manager.
	 * 
	 * @param String
	 * @return {@link KnowledgeManager} 
	 * 				object containing values for the specified knowledge paths.
	 */
	public KnowledgeManager createReplica(String id);
	
	/**
	 * Removes a replica knowledge manager from the container and return it.
	 * 
	 * @param {@link KnowledgeManager}
	 * @return {@link KnowledgeManager} 
	 * 				object containing values for the specified knowledge paths
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
	 * Retrieves values for the collection of the {@link KnowledgePath} objects.
	 * 
	 * @param {@link ReplicaListener}
	 * 				listens for adding replicas to the container or removing them from it.
	 */
	public void registerReplicaListener(ReplicaListener listener);
}
