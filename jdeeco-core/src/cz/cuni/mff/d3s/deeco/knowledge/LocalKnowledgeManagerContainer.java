package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * This container abstract class allows to keep with local knowledge managers. It gives the ability to implement the creation of new local knowledge manager 
 * and add it to the container, and to define how to retrieve all available local knowledge managers in this container.  
 * In addition, it specify a method to register a new local listener to listen for events caused by local knowledge managers changes (i.e. add/remove local knowledge manager). 
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public abstract class LocalKnowledgeManagerContainer {

	protected final List<KnowledgeManager> locals;
	protected final List<LocalListener> localListeners;
	
	public LocalKnowledgeManagerContainer() {
		this.locals = new LinkedList<>();
		this.localListeners = new LinkedList<>();
	}
	
	/**
	 * Creates a new instance of local knowledge manager with the specified id, add it to the container and register all existing local listener to it.
	 * 
	 * @param String
	 * 			the identifier of the knowledge manager
	 * @return {@link KnowledgeManager} 
	 * 			the newly created object containing values for the specified knowledge paths.
	 */
	protected KnowledgeManager createLocal(String id) {
		KnowledgeManager result = new CloningKnowledgeManager(id);
		locals.add(result);
		for (LocalListener listener : localListeners){
			listener.localCreated(result, this);
		}
		return result;
	}

	/**
	 * Removes a local knowledge manager from the container and return it. This implies also informing the local listener about removing this knowledge manager. 
	 * 
	 * @param {@link KnowledgeManager}
	 * 				local knowledge manager to be removed 
	 * @return {@link KnowledgeManager} 
	 * 				the removed local knowledge manager object containing values for the specified knowledge paths
	 */
	protected KnowledgeManager removeLocal(KnowledgeManager km) {
		KnowledgeManager kmVar = null;
		if (locals.contains(km)) {
			locals.remove(km);
			for (LocalListener listener : localListeners){
				listener.localRemoved(km, this);
			}
			kmVar = km;
		}
		return kmVar;
	}

	/**
	 * Retrieves all the local knowledge managers in the container.
	 * 
	 * @return List<{@link KnowledgeManager}> 
	 * 				object containing values for the specified knowledge paths
	 */
	public List<KnowledgeManager> getLocals() {
		return locals;
	}

	/**
	 * Adds the local listener to the container
	 * 
	 * @param {@link LocalListener}
	 * 				listens for adding local knowledge managers to the container or removing them.
	 */
	public void registerLocalListener(LocalListener listener) {
		if (!localListeners.contains(listener)){
			localListeners.add(listener);
		}
	}
}
