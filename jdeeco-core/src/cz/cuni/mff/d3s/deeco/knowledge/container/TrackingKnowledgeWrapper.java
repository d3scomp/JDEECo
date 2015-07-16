package cz.cuni.mff.d3s.deeco.knowledge.container;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

/**
 * 
 * @author Zbyněk Jiráček
 *
 */
public class TrackingKnowledgeWrapper extends ReadOnlyKnowledgeWrapper {

	private final KnowledgeManager knowledgeManager;
	private List<Object> trackedInstances;
	
	public TrackingKnowledgeWrapper(KnowledgeManager km){
		super(km);
		knowledgeManager = km;
		trackedInstances = new ArrayList<Object>();
	}
	
	public <TRole> TRole getTrackedRoleKnowledge(Class<TRole> roleClass) {
		TRole result = super.getUntrackedRoleKnowledge(roleClass);
		trackedInstances.add(result);
		return result;
	}
	
	public void ResetTracking() {
		trackedInstances.clear();
	}
	
	public void CommitChanges() {
		// TODO
	}

}