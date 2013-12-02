package cz.cuni.mff.d3s.deeco.knowledge;

/**
 * 
 * Gets called by a {@link ShadowKnowledgeManagerRegistry} when an event on a {@link LocalKnowledgeManagerContainer} (i.e. adding/removing local knowledge manager) 
 * matches the registered trigger.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface LocalListener {
	
	/**
	 * Gets the event of creating a new local knowledge manager in the local knowledge manager container.
	 * 
	 * @param {@link KnowledgeManager}
	 * 			manages the knowledge
	 * @param {@link LocalKnowledgeManagerContainer}
	 * 			accesses local knowledge managers
	 */
	public void localCreated(KnowledgeManager km, LocalKnowledgeManagerContainer container);
	
	/**
	 * Gets the event of removing a new local knowledge manager in the local knowledge manager container.
	 * 
	 * @param {@link KnowledgeManager}
	 * 			manages the knowledge
	 * @param {@link LocalKnowledgeManagerContainer}
	 * 			accesses local knowledge managers
	 */
	public void localRemoved(KnowledgeManager km, LocalKnowledgeManagerContainer container);
}
