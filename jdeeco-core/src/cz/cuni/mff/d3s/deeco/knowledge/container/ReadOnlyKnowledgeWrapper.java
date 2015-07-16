package cz.cuni.mff.d3s.deeco.knowledge.container;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 
 * @author Zbyněk Jiráček
 *
 */
public class ReadOnlyKnowledgeWrapper {

	private ReadOnlyKnowledgeManager knowledgeManager;
	
	public ReadOnlyKnowledgeWrapper(ReadOnlyKnowledgeManager km) {
		knowledgeManager = km;
	}
	
	public String getComponentId() {
		return knowledgeManager.getId();
	}

	public Collection<Class<?>> getRoles() {
		return Arrays.asList(knowledgeManager.getRoles());
	}
	
	public boolean hasRole(Class<?> roleClass) {
		return getRoles().contains(roleClass);
	}
	
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
	
	protected Map<Field, KnowledgePath> getFieldKnowledgePaths(List<Field> knowledgeFields) throws KnowledgeAccessException {
		Map<Field, KnowledgePath> result = new HashMap<>();
		for (Field knowledgeField : knowledgeFields) {
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
