package cz.cuni.mff.d3s.deeco.knowledge.container;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.deeco.annotations.checking.RoleAnnotationsHelper;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * A knowledge wrapper wraps a single component knowledge manager and allows for accessing the knowledge in
 * an object-oriented way. A tracking knowledge wrapper requires a {@link KnowledgeManager} instance
 * for accessing and updating the component knowledge. If the knowledge is only read and never updated,
 * or if the caller has only {@link ReadOnlyKnowledgeManager}, {@link ReadOnlyKnowledgeWrapper} can be used.
 * TrackingKnowledgeWrapper is an extension of the {@link ReadOnlyKnowledgeWrapper}.  
 *
 * A knowledge wrapper uses roles for accessing the knowledge. If a DEECo
 * component implements a particular role R (represented by a class R), one can acquire an instance of R
 * from the knowledge of the component using the {@link #getTrackedRoleKnowledge(Class)}. The returned
 * instance is tracked by the knowledge wrapper, which allows the caller to modify it and then update
 * the changes in the knowledge manager by calling the {@link #commitChanges()} method.
 * 
 * A role class is defined using the {@link cz.cuni.mff.d3s.deeco.annotations.Role}
 * annotation. A DEECo component implements the role using the
 * {@link cz.cuni.mff.d3s.deeco.annotations.PlaysRole} annotation. For more information
 * about roles, see the respective documentation.
 * 
 * @author Zbyněk Jiráček
 * 
 * @see Role
 * @see PlaysRole
 * @see TrackingKnowledgeContainer
 *
 */
public class TrackingKnowledgeWrapper extends ReadOnlyKnowledgeWrapper {

	private final KnowledgeManager knowledgeManager;
	private Map<Class<?>, Object> trackedInstances;
	private final RoleDisjointednessChecker roleDisjointednessChecker;
	
	/**
	 * Constructor used for debugging purposes. Use {@link #TrackingKnowledgeWrapper(KnowledgeManager)} constructor instead.
	 * @param km The knowledge manager for reading/updating the component knowledge
	 * @param roleDisjointednessChecker Used for checking that the roles used have disjoint member fields. See the {@link #commitChanges()} method for more details.
	 */
	public TrackingKnowledgeWrapper(KnowledgeManager km, RoleDisjointednessChecker roleDisjointednessChecker) {
		super(km);
		knowledgeManager = km;
		trackedInstances = new HashMap<Class<?>, Object>();
		this.roleDisjointednessChecker = roleDisjointednessChecker;
	}
	
	public TrackingKnowledgeWrapper(KnowledgeManager km) {
		this(km, new RoleDisjointednessChecker());
	}

	/**
	 * Gets the knowledge of the underlying component as an instance of the role class. The role class must
	 * be explicitly implemented by the component (using the {@link PlaysRole} annotation), otherwise the call
	 * will fail. The returned instance is tracked and when the {@link #commitChanges()} method is called,
	 * all changes in the instance are propagated back into the underlying knowledge manager.
	 * 
	 * @param roleClass A role class whose instance should be returned.
	 * @return Instance of the role class representing the knowledge.
	 * @throws RoleClassException Thrown when a role is not implemented or the role class cannot be instantiated.
	 * @throws KnowledgeAccessException Thrown when the knowledge contained in the role class cannot be acquired from the knowledge manager.
	 */
	@SuppressWarnings("unchecked")
	public <TRole> TRole getTrackedRoleKnowledge(Class<TRole> roleClass) throws RoleClassException, KnowledgeAccessException {
		if (trackedInstances.containsKey(roleClass)) {
			return (TRole) trackedInstances.get(roleClass);
		}
		
		TRole result = super.getUntrackedRoleKnowledge(roleClass);
		trackedInstances.put(roleClass, result);
		return result;
	}
	
	/**
	 * Resets the list of tracked role class instances created when the {@link #getTrackedRoleKnowledge(Class)}
	 * method is called.
	 */
	public void resetTracking() {
		trackedInstances.clear();
	}
	
	/**
	 * Commits all changes made to the tracked instances of role classes (earlier acquired using the
	 * {@link #getTrackedRoleKnowledge(Class)} method, if {@link #resetTracking()} was not called.
	 * 
	 * It is possible to track multiple role instances from the same knowledge wrapper (which implies the same knowledge manager),
	 * but there are restrictions. Currently, the component roles are considered to have disjunctive member fields.
	 * Overlapping member fields could cause conflicts when committing to the knowledge manager and the behavior
	 * in the case of overlapping roles is not defined.
	 * 
	 * @throws KnowledgeCommitException
	 * @throws KnowledgeAccessException
	 * @throws RoleClassException
	 */
	public void commitChanges() throws KnowledgeCommitException, KnowledgeAccessException, RoleClassException {
		List<Class<?>> trackedRoleClasses = trackedInstances.values().stream().map(o -> o.getClass()).collect(Collectors.toList());
		roleDisjointednessChecker.checkRolesAreDisjoint(trackedRoleClasses);
		
		for (Object trackedInstance : trackedInstances.values()) {
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
			if (field.getName().equals("id")) {
				continue;
			}
			
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