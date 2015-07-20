package cz.cuni.mff.d3s.deeco.knowledge.container;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.annotations.checking.RoleAnnotationsHelper;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * 
 * @author Zbyněk Jiráček
 *
 */
public class TrackingKnowledgeWrapper extends ReadOnlyKnowledgeWrapper {

	private final KnowledgeManager knowledgeManager;
	private List<Object> trackedInstances;
	private final RoleDisjointednessChecker roleDisjointednessChecker;
	
	public TrackingKnowledgeWrapper(KnowledgeManager km, RoleDisjointednessChecker roleDisjointednessChecker) {
		super(km);
		knowledgeManager = km;
		trackedInstances = new ArrayList<Object>();
		this.roleDisjointednessChecker = roleDisjointednessChecker;
	}
	
	public TrackingKnowledgeWrapper(KnowledgeManager km) {
		this(km, new RoleDisjointednessChecker());
	}
	
	public <TRole> TRole getTrackedRoleKnowledge(Class<TRole> roleClass) throws RoleClassException, KnowledgeAccessException {
		TRole result = super.getUntrackedRoleKnowledge(roleClass);
		trackedInstances.add(result);
		return result;
	}
	
	public void resetTracking() {
		trackedInstances.clear();
	}
	
	public void commitChanges() throws KnowledgeCommitException, KnowledgeAccessException, RoleClassException {
		List<Class<?>> trackedRoleClasses = trackedInstances.stream().map(o -> o.getClass()).collect(Collectors.toList());
		roleDisjointednessChecker.checkRolesAreDisjoint(trackedRoleClasses);
		
		for (Object trackedInstance : trackedInstances) {
			Class<?> roleClass = trackedInstance.getClass();
			List<Field> knowledgeFields = RoleAnnotationsHelper.getNonLocalKnowledgeFields(roleClass, false);
			Map<Field, KnowledgePath> fieldKnowledgePaths = getFieldKnowledgePaths(knowledgeFields);
			
			ChangeSet changeSet = createChangeSet(fieldKnowledgePaths, trackedInstance);
			try {
				knowledgeManager.update(changeSet);
			} catch (KnowledgeUpdateException e) {
				throw new KnowledgeCommitException(String.format("Failed to update the knowledge of the component '%s': %s",
						getComponentId(), e.getMessage()), e);
			}
		}
	}
	
	private ChangeSet createChangeSet(Map<Field, KnowledgePath> fieldKnowledgePaths, Object trackedInstance) throws RoleClassException {
		ChangeSet result = new ChangeSet();
		
		for (Entry<Field, KnowledgePath> fieldAndValue : fieldKnowledgePaths.entrySet()) {
			Field field = fieldAndValue.getKey();
			KnowledgePath knowledgePath = fieldAndValue.getValue();
			try {
				result.setValue(knowledgePath, field.get(trackedInstance));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RoleClassException(String.format("Failed to get the value of the field '%s' of the role class '%s'.",
						field.getName(), field.getDeclaringClass().getName()), e);
			}
		}
		
		return result;
	}

}