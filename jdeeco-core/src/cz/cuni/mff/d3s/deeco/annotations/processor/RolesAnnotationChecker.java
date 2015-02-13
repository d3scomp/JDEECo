package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.CoordinatorRole;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.MemberRole;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;

/**
 * All checks associated with roles.
 * Used by {@link AnnotationProcessor}.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class RolesAnnotationChecker {
		
	/**
	 * Checks that a component instance class correctly implements all roles that are declared
	 * by the {@link PlaysRole} annotation. The function does not return any value, it either
	 * succeeds or throws an exception.
	 * @param obj The componentInstance
	 * @throws AnnotationProcessorException 
	 */
	public void checkRolesImplementation(Object obj) throws AnnotationProcessorException {
		if (obj == null) {
			throw new AnnotationProcessorException("The input instance cannot be null.");
		}
		
		Class<?> componentClass = obj.getClass();
		if (!componentClass.isAnnotationPresent(Component.class)) {
			throw new AnnotationProcessorException("The input instance is not a component (the class is not annotated by the @" + Component.class.getSimpleName() + " annotation).");
		}
		
		PlaysRole[] roleAnnotations = componentClass.getAnnotationsByType(PlaysRole.class);
		if (roleAnnotations.length == 0) {
			// no roles, everything ok
			return;
		}
		
		List<Field> knowledgeFields = getNonLocalKnowledgeFields(componentClass, false);
		
		for (PlaysRole role : roleAnnotations) {
			Class<?> roleClass = role.value();
			if (!roleClass.isAnnotationPresent(Role.class)) {
				throw new AnnotationProcessorException("The class " + roleClass.getSimpleName() + " is used as a role class, but it is not annotated by the @" + Role.class.getSimpleName() + " annotation.");
			}
			
			checkRoleFieldsImplementation(knowledgeFields, roleClass);
		}
		
	}
	
	public void checkRolesImplementation(List<Parameter> parameters, CoordinatorRole[] coordinatorRoleAnnotations, 
			MemberRole[] memberRoleAnnotations) throws AnnotationProcessorException {
		if (parameters == null) {
			throw new AnnotationProcessorException("The input parameters cannot be null.");
		}
		if (coordinatorRoleAnnotations == null) {
			throw new AnnotationProcessorException("The coordinatorRoles parameter cannot be null.");
		}
		if (memberRoleAnnotations == null) {
			throw new AnnotationProcessorException("The memberRoles parameter cannot be null.");
		}
		
		for (Parameter parameter : parameters) {
			checkKnowledgePath(parameter.getGenericType(), parameter.getKnowledgePath(), coordinatorRoleAnnotations,
					memberRoleAnnotations);
		}
		
	}
	
	private void checkKnowledgePath(Type type, KnowledgePath knowledgePath, CoordinatorRole[] coordinatorRoleAnnotations, 
			MemberRole[] memberRoleAnnotations) throws AnnotationProcessorException {
		if (knowledgePath.getNodes().size() < 2) {
			throw new AnnotationProcessorException("A knowledge path must contain at least two elements (coord/member and field name).");
		}
		
		// just choose coordinator / member role classes
		List<Class<?>> roleClasses = new ArrayList<>();
		PathNode first = knowledgePath.getNodes().get(0);
		if (first instanceof PathNodeCoordinator) {
			for (CoordinatorRole role : coordinatorRoleAnnotations) {
				roleClasses.add(role.value());
			}
		} else if (first instanceof PathNodeMember) {
			for (MemberRole role : memberRoleAnnotations) {
				roleClasses.add(role.value());
			}
		} else {
			throw new AnnotationProcessorException("A knowledge path does not start with coord/member.");
		}
		
		// go through the path, evaluate inner knowledge paths of PathNodeMapKey-s
		PathNode second = knowledgePath.getNodes().get(1);
		List<String> fieldNameSequence = new ArrayList<>();
		if (second instanceof PathNodeComponentId) {
			fieldNameSequence.add("id");
		} else if (second instanceof PathNodeField) {
			for (int i = 1; i < knowledgePath.getNodes().size(); i++) {
				PathNode pn = knowledgePath.getNodes().get(i);
				if (pn instanceof PathNodeField) {
					fieldNameSequence.add(((PathNodeField)pn).getName());
				} else if (pn instanceof PathNodeMapKey) {
					checkKnowledgePath(/*TODO*/null, ((PathNodeMapKey)pn).getKeyPath(), coordinatorRoleAnnotations,
							memberRoleAnnotations);
					break; // we don't check after [], but we actually could
				}
			}
		} else {
			throw new AnnotationProcessorException("A knowledge path's second element should be a field name.");
		}

		// test the knowledge path against all roles
		for (Class<?> roleClass : roleClasses) {
			if (!isFieldInRole(type, fieldNameSequence, roleClass)) {
				throw new AnnotationProcessorException("The knowledge path '" + knowledgePath.toString() 
						+ "' is not valid for the role '" + roleClass.getSimpleName() + "'.");
			}
		}
	}
	
	boolean isFieldInRole(Type type, List<String> fieldNameSequence, Class<?> roleClass) {
		Type fieldType = getTypeInRole(fieldNameSequence, roleClass);
		if (fieldType == null) {
			return false; // field not present in the role class
		}
		
		return type == null || compareTypes(type, fieldType);
		
	}
	
	private Type getTypeInRole(List<String> fieldNameSequence, Class<?> roleClass) {
		return null;
	}

	/**
	 * Returns all fields of a class that are interpreted as a public knowledge, i.e. all public
	 * non-static fields that are not annotated by the {@link Local} attribute.
	 * @param clazz The meta-class
	 * @return List of fields
	 */
	private List<Field> getNonLocalKnowledgeFields(Class<?> clazz, boolean warnings) {
		List<Field> knowledgeFields = new ArrayList<>();
		for (Field field : clazz.getFields()) {
			if (Modifier.isStatic(field.getModifiers()) || !Modifier.isPublic(field.getModifiers())) {
				if (warnings) {
					Log.i("Class " + clazz.getSimpleName() + ": Field " + field.getName() + " ignored (is it public and non-static?).");
				}
				continue; // static or non-public fields are not put into the knowledge
			}
			
			if (field.isAnnotationPresent(Local.class))
				continue; // local fields are not shared (are they? TODO)
			
			knowledgeFields.add(field);
		}
		
		return knowledgeFields;
	}
	
	/**
	 * Checks that all fields from a particular role are implemented in a given class.
	 * The function does not return a value, it either succeeds, or it throws an exception.
	 * @param knowledgeFields Fields of the component class (can be obtained by {@link RolesAnnotationChecker#getNonLocalKnowledgeFields})
	 * @param roleClass The class representing a role
	 * @throws AnnotationProcessorException
	 */
	private void checkRoleFieldsImplementation(List<Field> knowledgeFields, Class<?> roleClass) throws AnnotationProcessorException {
		List<Field> roleFields = getNonLocalKnowledgeFields(roleClass, true);
		
		for (Field roleField : roleFields) {
			if (!isRoleFieldImplemented(knowledgeFields, roleField)) {
				throw new AnnotationProcessorException("The field " + roleClass.getSimpleName() + "."
						+ roleField.getName() + " is not implemented (or has a different type than " 
						+ roleField.getGenericType() + ").");
			}
		}
	}
	
	/**
	 * Checks whether a given field is implemented. It returns true if the field (with the same name
	 * and of the same type) is implemented (present in a list of fields), false otherwise.
	 * @param knowledgeFields Fields of the component class (can be obtained by {@link RolesAnnotationChecker#getNonLocalKnowledgeFields}) 
	 * @param roleField The field that should be implemented (by the role definition)
	 * @return True if the field is implemented, false otherwise
	 */
	private boolean isRoleFieldImplemented(List<Field> knowledgeFields, Field roleField) {
		for (Field knowledgeField : knowledgeFields) {
			if (knowledgeField.getName().equals(roleField.getName())) {
				Type knowledgeFieldType = knowledgeField.getGenericType();
				Type roleFieldType = roleField.getGenericType();
				return compareTypes(knowledgeFieldType, roleFieldType);
				/*Class<?> knowledgeFieldClass = knowledgeField.getType();
				Class<?> roleFieldClass = roleField.getType();
				return (roleFieldClass.equals(knowledgeFieldClass));*/
			}
		}
		
		return false; // no field with equal name
	}
	
	private boolean compareTypes(Type implementationType, Type roleType) {
		return implementationType.equals(roleType);
	}
	
}
