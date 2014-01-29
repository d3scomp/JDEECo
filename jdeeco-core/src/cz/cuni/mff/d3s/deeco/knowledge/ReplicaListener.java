package cz.cuni.mff.d3s.deeco.knowledge;

/**
 * Gets called by a  {@link ShadowKnowledgeManagerRegistry} when an event on a  {@link KnowledgeManagerContainer} (i.e. adding/removing replica knowledge manager) 
 * matches the registered trigger.

 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface ReplicaListener {

	/**
	 * Gets the event of registering a new replica knowledge manager in the replica knowledge manager container.
	 * 
	 * @param {@link KnowledgeManager}
	 * 			manages the knowledge
	 * @param {@link KnowledgeManagerContainer}
	 * 			accesses replica knowledge managers
	 */
	public void replicaRegistered(KnowledgeManager km, KnowledgeManagerContainer container);

	/**
	 * Gets the event of removing a new replica knowledge manager in the local knowledge manager container.
	 * 
	 * @param {@link KnowledgeManager}
	 * 			manages the knowledge
	 * @param {@link KnowledgeManagerContainer}
	 * 			accesses replica knowledge managers
	 */
	public void replicaUnregistered(KnowledgeManager km, KnowledgeManagerContainer container);
}
