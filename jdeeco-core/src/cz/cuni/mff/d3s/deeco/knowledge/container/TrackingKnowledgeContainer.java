package cz.cuni.mff.d3s.deeco.knowledge.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;

/**
 * An implementation of the {@link KnowledgeContainer} interface that is using knowledge managers.
 * 
 * The tracking knowledge container creates a wrapper above all knowledge managers (one local and multiple shadow)
 * and allows for acquiring the knowledge of the components in an object-oriented way using roles.
 * 
 * @author Zbyněk Jiráček
 * 
 * @see KnowledgeContainer
 * @see KnowledgeManager
 *
 */
public class TrackingKnowledgeContainer implements KnowledgeContainer {

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
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer#getUntrackedKnowledgeForRole(java.lang.Class)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer#getTrackedKnowledgeForRole(java.lang.Class)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer#resetTracking()
	 */
	@Override
	public void resetTracking() {
		localKnowledgeContainer.resetTracking();
		// shadow knowledge is read only
	}
	
	/**
	 * @see cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer#commitChanges()

	 * PLEASE NOTE that currently, only data from the local knowledge manager can be updated. Shadow knowledge managers
	 * are just read-only, therefore the changes to the shadow knowledge are discarded.
	 */
	@Override
	public void commitChanges() throws KnowledgeContainerException {
		try {
			localKnowledgeContainer.commitChanges();
			// shadow knowledge is read only
		} catch (KnowledgeCommitException | KnowledgeAccessException | RoleClassException e) {
			throw new KnowledgeContainerException(e.getMessage(), e);
		}
	}

}
