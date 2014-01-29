package cz.cuni.mff.d3s.deeco.knowledge;

/**
 * 
 * Gets called by a {@link ShadowKnowledgeManagerRegistry} when an event on a {@link KnowledgeManagerContainer} matches the registered trigger.
 * The events can be creating a new local knowledge manager or removing a specific local knowledge manager from the local knowledge manager container.  
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
	 * @param {@link KnowledgeManagerContainer}
	 * 			accesses local knowledge managers
	 */
	public void localCreated(KnowledgeManager km, KnowledgeManagerContainer container);
	
	/**
	 * Gets the event of removing a new local knowledge manager in the local knowledge manager container.
	 * 
	 * @param {@link KnowledgeManager}
	 * 			manages the knowledge
	 * @param {@link KnowledgeManagerContainer}
	 * 			accesses local knowledge managers
	 */
	public void localRemoved(KnowledgeManager km, KnowledgeManagerContainer container);
}
