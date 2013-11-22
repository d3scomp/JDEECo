package cz.cuni.mff.d3s.deeco.knowledge;

/**
 * Gets called by a  {@link ShadowKnowledgeManagerRegistry} when an event on a  {@link ReplicaKnowledgeManagerContainer} (i.e. adding/removing replica knowledge manager) 
 * matches the registered trigger.

 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface ReplicaListener {

	/**
	 * Gets the event of creating a new replica knowledge manager in the replica knowledge manager container.
	 * 
	 * @param {@link KnowledgeManager}
	 * 			manages the knowledge
	 * @param {@link ReplicaKnowledgeManagerContainer}
	 * 			accesses replica knowledge managers
	 */
	public void replicaCreated(KnowledgeManager km, ReplicaKnowledgeManagerContainer container);

	/**
	 * Gets the event of removing a new replica knowledge manager in the local knowledge manager container.
	 * 
	 * @param {@link KnowledgeManager}
	 * 			manages the knowledge
	 * @param {@link ReplicaKnowledgeManagerContainer}
	 * 			accesses replica knowledge managers
	 */
	public void replicaRemoved(KnowledgeManager km, ReplicaKnowledgeManagerContainer container);
}
