package cz.cuni.mff.d3s.deeco.knowledge.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;

/**
 * 
 * @author Zbyněk Jiráček
 *
 */
public class TrackingKnowledgeContainer {

	private final TrackingKnowledgeWrapper localKnowledgeContainer;
	private final Collection<ReadOnlyKnowledgeWrapper> shadowKnowledgeContainers; 
	
	public TrackingKnowledgeContainer(KnowledgeManager localKnowledgeManager, 
			Collection<ReadOnlyKnowledgeManager> shadowKnowledgeManagers) {
		localKnowledgeContainer = new TrackingKnowledgeWrapper(localKnowledgeManager);
		shadowKnowledgeContainers = shadowKnowledgeManagers.stream().map(km -> new ReadOnlyKnowledgeWrapper(km)).collect(Collectors.toList());
	}
	
	private Collection<ReadOnlyKnowledgeWrapper> getAllKnowledgeContainers() {
		List<ReadOnlyKnowledgeWrapper> result = new ArrayList<>(shadowKnowledgeContainers);
		result.add(localKnowledgeContainer);
		return result;
	}
		
	public <TRole> Collection<TRole> getUntrackedKnowledgeForRole(Class<TRole> roleClass) {
		List<TRole> result = new ArrayList<>();
		
		for (ReadOnlyKnowledgeWrapper kc : getAllKnowledgeContainers()) {
			if (kc.hasRole(roleClass)) {
				result.add(kc.getUntrackedRoleKnowledge(roleClass));
			}
		}
		
		return result;
	}
	
	public <TRole> Collection<TRole> getTrackedKnowledgeForRole(Class<TRole> roleClass) {
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
	}
	
	public void ResetTracking() {
		localKnowledgeContainer.ResetTracking();
		// shadow knowledge is read only
	}
	
	public void CommitChanges() {
		localKnowledgeContainer.CommitChanges();
		// shadow knowledge is read only
	}

}
