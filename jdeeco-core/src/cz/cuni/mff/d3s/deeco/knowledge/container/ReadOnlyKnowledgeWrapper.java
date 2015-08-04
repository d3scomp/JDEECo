package cz.cuni.mff.d3s.deeco.knowledge.container;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.deeco.annotations.checking.RoleAnnotationsHelper;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;

/**
 * A knowledge wrapper wraps a single component knowledge manager and allows for accessing the knowledge in
 * an object-oriented way. A read-only knowledge wrapper uses a {@link ReadOnlyKnowledgeManager} instance
 * for accessing the component knowledge. 

 * A knowledge wrapper uses roles for accessing the knowledge. If a DEECo
 * component implements a particular role R (represented by a class R), one can acquire an instance of R
 * from the knowledge of the component using the {@link #getUntrackedRoleKnowledge(Class)}. In order to
 * change the knowledge, {@link TrackingKnowledgeContainer} needs to be used.
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
public class ReadOnlyKnowledgeWrapper {

	private ReadOnlyKnowledgeManager knowledgeManager;
	
	public ReadOnlyKnowledgeWrapper(ReadOnlyKnowledgeManager km) {
		knowledgeManager = km;
	}
	
	/**
	 * Gets the ID of the component knowledge manager (which should correspond to the component ID).
	 * @return The component ID.
	 */
	public String getComponentId() {
		return knowledgeManager.getId();
	}

	/**
	 * Gets all roles that are implemented by the component's knowledge.
	 * @return Collection of role classes.
	 */
	protected Collection<Class<?>> getRoles() {
		return Arrays.asList(knowledgeManager.getRoles());
	}
	
	/**
	 * Determines whether the underlying component's knowledge implements a particular role.
	 * @param roleClass A role class to be implemented.
	 * @return True if the role is implemented, false otherwise.
	 */
	public boolean hasRole(Class<?> roleClass) {
		return getRoles().contains(roleClass);
	}
	
	/**
	 * Gets the knowledge of the underlying component as an instance of the role class. The role class must
	 * be explicitly implemented by the component (using the {@link PlaysRole} annotation), otherwise the call
	 * will fail.
	 * @param roleClass A role class whose instance should be returned.
	 * @return Instance of the role class representing the knowledge.
	 * @throws RoleClassException Thrown when a role is not implemented or the role class cannot be instantiated.
	 * @throws KnowledgeAccessException Thrown when the knowledge contained in the role class cannot be acquired from the knowledge manager.
	 */
	public <TRole> TRole getUntrackedRoleKnowledge(Class<TRole> roleClass) throws RoleClassException, KnowledgeAccessException {
		TRole result;

		if (!hasRole(roleClass)) {
			throw new RoleClassException(String.format("The role class '%s' is not implemented by the knowledge of the component '%s'.",
					roleClass.getName(), getComponentId()));
		}
		
		try {
			result = roleClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RoleClassException(String.format("The role class '%s' could not be instantiated.", roleClass.getName()), e);
		}
		
		try {
			List<Field> knowledgeFields = RoleAnnotationsHelper.getNonLocalKnowledgeFields(roleClass, false);
			Map<Field, KnowledgePath> fieldKnowledgePaths = getFieldKnowledgePaths(knowledgeFields);
			ValueSet knowledgeValues;
			knowledgeValues = knowledgeManager.get(fieldKnowledgePaths.values());
			for (Field knowledgeField : knowledgeFields) {
				try {
					Object fieldValue = knowledgeValues.getValue(fieldKnowledgePaths.get(knowledgeField));
					knowledgeField.set(result, fieldValue);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RoleClassException(String.format("Failed to set the value to the field '%s' of the role class '%s'.",
							knowledgeField.getName(), roleClass.getName()), e);
				}
			}
		} catch (KnowledgeNotFoundException e) {
			throw new KnowledgeAccessException(String.format("The knowledge path '%s' could not be found in the knowledge manager of the component '%s' (though the knowledge declares that it supports the role '%s').",
					e.getNotFoundPath(), getComponentId(), roleClass.getName()), e);
		}
		
		return result;
	}
	
	// lists the role class fields by introspection and returns of respective knowledge paths
	protected Map<Field, KnowledgePath> getFieldKnowledgePaths(List<Field> knowledgeFields) throws KnowledgeAccessException {
		Map<Field, KnowledgePath> result = new HashMap<>();
		for (Field knowledgeField : knowledgeFields) {
			if (knowledgeField.getName().equals("id")) {
				continue; // we can't update ID
			}
			
			try {
				KnowledgePath knowledgePath;
				knowledgePath = KnowledgePathHelper.createKnowledgePath(knowledgeField.getName(), PathOrigin.COMPONENT);
				result.put(knowledgeField, knowledgePath);
			} catch (ParseException | AnnotationProcessorException e) {
				throw new KnowledgeAccessException(String.format("The knowledge path for the field '%s.%s' could not be created.",
						knowledgeField.getDeclaringClass().getName(), knowledgeField.getName()), e);
			}
		}
		
		return result;
	}
}
