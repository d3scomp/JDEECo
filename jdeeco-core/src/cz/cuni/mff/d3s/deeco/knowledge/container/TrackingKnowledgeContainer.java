package cz.cuni.mff.d3s.deeco.knowledge.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;

/**
 * Knowledge container is useful when operating with multiple component's knowledge (one local knowledge and multiple
 * shadow knowledge instances).
 * 
 * A knowledge container creates a wrapper above all knowledge managers and allows for acquiring the knowledge of
 * the components in an object-oriented way using roles.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class TrackingKnowledgeContainer {

	private final TrackingKnowledgeWrapper localKnowledgeContainer;
	private final Collection<ReadOnlyKnowledgeWrapper> shadowKnowledgeContainers; 
	
	public static TrackingKnowledgeContainer createFromKnowledgeManagers(
			KnowledgeManager localKnowledgeManager, 
			Collection<ReadOnlyKnowledgeManager> shadowKnowledgeManagers) {
		
		TrackingKnowledgeWrapper localKnowledgeContainer = new TrackingKnowledgeWrapper(localKnowledgeManager);
		Collection<ReadOnlyKnowledgeWrapper> shadowKnowledgeContainers = shadowKnowledgeManagers.stream().map(km -> new ReadOnlyKnowledgeWrapper(km)).collect(Collectors.toList());
		return new TrackingKnowledgeContainer(localKnowledgeContainer, shadowKnowledgeContainers); 
	}
	
	public TrackingKnowledgeContainer(
			TrackingKnowledgeWrapper localKnowledgeContainer,
			Collection<ReadOnlyKnowledgeWrapper> shadowKnowledgeContainers) {
		
		this.localKnowledgeContainer = localKnowledgeContainer;
		this.shadowKnowledgeContainers = shadowKnowledgeContainers;
	}

	private Collection<ReadOnlyKnowledgeWrapper> getAllKnowledgeWrappers() {
		List<ReadOnlyKnowledgeWrapper> result = new ArrayList<>(shadowKnowledgeContainers);
		result.add(localKnowledgeContainer);
		return result;
	}
	
	/**
	 * Traverses through all underlying knowledge managers and for each knowledge implementing the specified
	 * role it returns an instance of the role class. This instance represents a view on the component's
	 * knowledge.
	 * @param roleClass The role class.
	 * @return Instances of the role class for each component that implements the specified role.
	 * @throws KnowledgeContainerException
	 */
	public <TRole> Collection<TRole> getUntrackedKnowledgeForRole(Class<TRole> roleClass) throws KnowledgeContainerException {
		try {
			List<TRole> result = new ArrayList<>();
			
			for (ReadOnlyKnowledgeWrapper kc : getAllKnowledgeWrappers()) {
				if (kc.hasRole(roleClass)) {
					result.add(kc.getUntrackedRoleKnowledge(roleClass));
				}
			}
			
			return result;
			
		} catch (RoleClassException | KnowledgeAccessException e) {
			throw new KnowledgeContainerException(e.getMessage(), e);
		}
	}
	
	/**
	 * Similarly to the {@link #getUntrackedKnowledgeForRole(Class)} it returns role view on the components'
	 * knowledge. Additionally, the returned instances are tracked, which means that if the caller modifies
	 * the returned instances and calls the {@link #commitChanges()} method, the changes will be propagated
	 * into the component knowledge manager.
	 * @param roleClass The role class.
	 * @return Instances of the role class for each component that implements the specified role.
	 * @throws KnowledgeContainerException
	 *
	 * @see #getUntrackedKnowledgeForRole(Class)
	 * @see #commitChanges()
	 * @see #resetTracking()
	 */
	public <TRole> Collection<TRole> getTrackedKnowledgeForRole(Class<TRole> roleClass) throws KnowledgeContainerException {
		try {
			List<TRole> result = new ArrayList<>();
			
			// the local knowledge changes will be tracked
			if (localKnowledgeContainer.hasRole(roleClass)) {
				result.add(localKnowledgeContainer.getTrackedRoleKnowledge(roleClass));
			}
			
			// but the shadow knowledge is read-only, therefore the changes won't be tracked
			for (ReadOnlyKnowledgeWrapper kc : shadowKnowledgeContainers) {
				if (kc.hasRole(roleClass)) {
					result.add(kc.getUntrackedRoleKnowledge(roleClass));
				}
			}
			
			return result;
			
		} catch (RoleClassException | KnowledgeAccessException e) {
			throw new KnowledgeContainerException(e.getMessage(), e);
		}
	}
	
	/**
	 * Clears the list of tracked role class instances returned by the {@link #getTrackedKnowledgeForRole(Class)} method.
	 * Consequently, changes to the earlier returned role class instances cannot be committed into the knowledge manager anymore.
	 * 
	 * @see #commitChanges()
	 * @see #getTrackedKnowledgeForRole(Class)
	 */
	public void resetTracking() {
		localKnowledgeContainer.resetTracking();
		// shadow knowledge is read only
	}
	
	/**
	 * Commits all the changes made to the tracked role class instances returned by the {@link #getTrackedKnowledgeForRole(Class)}
	 * method (if the tracking was not reset by the {@link #resetTracking()} method). All values in the tracked role
	 * class instances are stored into the respective knowledge managers.
	 * 
	 * PLEASE NOTE that currently, only data from the local knowledge manager can be updated. Shadow knowledge managers
	 * are just read-only, therefore the changes to the shadow knowledge are discarded.
	 * 
	 * @throws KnowledgeContainerException
	 * 
	 * @see #getTrackedKnowledgeForRole(Class)
	 * @see #resetTracking()
	 */
	public void commitChanges() throws KnowledgeContainerException {
		try {
			localKnowledgeContainer.commitChanges();
			// shadow knowledge is read only
		} catch (KnowledgeCommitException | KnowledgeAccessException | RoleClassException e) {
			throw new KnowledgeContainerException(e.getMessage(), e);
		}
	}

}
