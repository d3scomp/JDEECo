package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.CoordinatorRole;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.MemberRole;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
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
public class RolesAnnotationChecker implements AnnotationChecker {
	
	public void validateComponent(Object componentObj, ComponentInstance componentInstance) throws AnnotationCheckerException {		
		checkRolesImplementation(componentObj);
	}
	
	public void validateEnsemble(Class<?> ensembleClass, EnsembleDefinition ensembleDefinition) throws AnnotationCheckerException {
		if (ensembleClass == null) {
			throw new AnnotationCheckerException("The input ensemble class cannot be null.");
		}
		if (ensembleDefinition == null) {
			throw new AnnotationCheckerException("The input ensemble definition cannot be null.");
		}
		
		Class<?>[] coordinatorRoles = RoleAnnotationsHelper.getCoordinatorRoleAnnotations(ensembleClass);
		Class<?>[] memberRoles = RoleAnnotationsHelper.getMemberRoleAnnotations(ensembleClass);
		if (coordinatorRoles.length > 1 || memberRoles.length > 1) {
			throw new AnnotationCheckerException("Only one CoordinatorRole and one MemberRole annotation is allowed per ensemble.");
		}
		
		checkRolesImplementation(ensembleDefinition.getMembership().getParameters(), coordinatorRoles, memberRoles);
		checkRolesImplementation(ensembleDefinition.getKnowledgeExchange().getParameters(), coordinatorRoles, memberRoles);
	}
	
	/**
	 * Checks that a component instance class correctly implements all roles that are declared
	 * by the {@link PlaysRole} annotation. The function does not return any value, it either
	 * succeeds or throws an exception.
	 * @param obj The componentInstance
	 * @throws AnnotationCheckerException 
	 */
	void checkRolesImplementation(Object obj) throws AnnotationCheckerException {
		if (obj == null) {
			throw new AnnotationCheckerException("The input instance cannot be null.");
		}
		
		Class<?> componentClass = obj.getClass();
		if (!componentClass.isAnnotationPresent(Component.class)) {
			throw new AnnotationCheckerException("The input instance is not a component (the class is not annotated by the @" + Component.class.getSimpleName() + " annotation).");
		}

		// check if the class contains String id
		List<Field> knowledgeFields = getNonLocalKnowledgeFields(componentClass, false);
		if (!isRoleFieldImplemented(knowledgeFields, String.class, "id")) {
			throw new AnnotationCheckerException("The field public String id, which is mandatory in component classes, is missing.");
		}
		
		// check if the class contains all fields from the role classes
		Class<?>[] roleAnnotations = RoleAnnotationsHelper.getPlaysRoleAnnotations(componentClass);
		for (Class<?> roleClass : roleAnnotations) {			
			checkRoleFieldsImplementation(knowledgeFields, roleClass);
		}		
	}
	
	/**
	 * Checks that all of the given parameter's knowledge paths exist in given roles. Works for only for
	 * ensembles membership condition and knowledge exchange functions.
	 * @param parameters The processed parameters of the function
	 * @param coordinatorRoleAnnotations An array of coordinator role classes
	 * @param memberRoleAnnotations An array of member role classes
	 * @throws AnnotationCheckerException
	 */
	void checkRolesImplementation(List<Parameter> parameters, Class<?>[] coordinatorRoles, 
			Class<?>[] memberRoles) throws AnnotationCheckerException {
		if (parameters == null) {
			throw new AnnotationCheckerException("The input parameters cannot be null.");
		}
		if (coordinatorRoles == null) {
			throw new AnnotationCheckerException("The coordinatorRoles parameter cannot be null.");
		}
		if (memberRoles == null) {
			throw new AnnotationCheckerException("The memberRoles parameter cannot be null.");
		}
		
		for (Parameter parameter : parameters) {
			Type parameterType;
			if (parameter.getKind() == ParameterKind.IN) {
				parameterType = parameter.getGenericType();
			} else {
				if (!(parameter.getGenericType() instanceof ParameterizedType)) {
					throw new AnnotationCheckerException("A parameter with different kind than IN must be wrapped in a generic type (ie. ParameterizedType)");
				}
				
				ParameterizedType parameterHolderType = (ParameterizedType) parameter.getGenericType();
				parameterType = parameterHolderType.getActualTypeArguments()[0];
			}
			
			checkKnowledgePath(parameterType, parameter.getKnowledgePath(), coordinatorRoles, memberRoles);
		}
		
	}
	
	/**
	 * Checks that a given knowledge path is valid in given roles. Works only for ensembles membership
	 * condition and knowledge exchange functions.
	 * @param type Type of the expression
	 * @param knowledgePath The knowledge path
	 * @param coordinatorRoleAnnotations An array of coordinator role classes
	 * @param memberRoleAnnotations An array of member role classes
	 * @throws AnnotationCheckerException
	 */
	private void checkKnowledgePath(Type type, KnowledgePath knowledgePath, Class<?>[] coordinatorRoles, 
			Class<?>[] memberRoles) throws AnnotationCheckerException {
		if (knowledgePath.getNodes().size() < 2) {
			throw new AnnotationCheckerException("A knowledge path must contain at least two elements (coord/member and field name).");
		}
		
		// just choose coordinator / member role classes
		Class<?>[] roleClasses;
		PathNode first = knowledgePath.getNodes().get(0);
		if (first instanceof PathNodeCoordinator) {
			roleClasses = coordinatorRoles;
		} else if (first instanceof PathNodeMember) {
			roleClasses = memberRoles;
		} else {
			throw new AnnotationCheckerException("A knowledge path does not start with coord/member.");
		}
		
		// go through the path, evaluate inner knowledge paths of PathNodeMapKey-s
		// TODO extract to individual method and use it to check component processes 
		// (that they use only fields available in the component)
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
					checkKnowledgePath(/*TODO*/null, ((PathNodeMapKey)pn).getKeyPath(), 
							coordinatorRoles, memberRoles);
					break; // we don't check after [], but we actually could
				}
			}
		} else {
			throw new AnnotationCheckerException("A knowledge path's second element should be a field name.");
		}

		// test the knowledge path against all roles
		for (Class<?> roleClass : roleClasses) {
			if (!isFieldInRole(type, fieldNameSequence, roleClass)) {
				throw new AnnotationCheckerException("The knowledge path '" + knowledgePath.toString() + "'"
						+ (type != null ? " of type " + type : "") + " is not valid for the role '" + roleClass.getSimpleName() + "'. "
						+ "Check whether the field (or sequence of fields) exists in the role and that it has correct type(s) and is public, nonstatic and non@Local");
			}
		}
	}

	/**
	 * Checks whether a field sequence is present in a given role class. Also checks the type
	 * of the field, if wanted. The given path can contain only field names (not [..]). It not
	 * only checks whether the given role class contains the first-level field, it also checks
	 * whether the rest of the path is valid.
	 * @param type The desired type (or null if the type should not be checked)
	 * @param fieldNameSequence The knowledge path separated to individual path nodes
	 * @param roleClass The class that should contain the path
	 * @return True if the field (of given type) is present in the role, false otherwise.
	 */
	boolean isFieldInRole(Type type, List<String> fieldNameSequence, Class<?> roleClass) throws AnnotationCheckerException {	
		if (fieldNameSequence == null || fieldNameSequence.size() < 1) {
			throw new AnnotationCheckerException("The field sequence cannot be null or empty.");
		}
		if (roleClass == null) {
			throw new AnnotationCheckerException("The role class cannot be null.");
		}
		
		Type fieldType = getTypeInRole(fieldNameSequence, roleClass);
		if (fieldType == null) {
			return false; // field not present in the role class
		}
		
		return type == null || compareTypes(type, fieldType);
		
	}
	
	private Type getTypeInRole(List<String> fieldNameSequence, Class<?> roleClass) {
		if (fieldNameSequence.size() == 1 && fieldNameSequence.get(0).equals("id")) {
			// id is always present and always of type String
			return String.class;
		}
		
		return getTypeInClass(fieldNameSequence, roleClass);
	}
	
	private Type getTypeInClass(List<String> fieldNameSequence, Class<?> clazz) {
		String firstField = fieldNameSequence.get(0);
		Field field;
		try {
			field = clazz.getField(firstField);
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
		
		if (!RoleAnnotationsHelper.isPublicAndNonstatic(field)) {
			return null;
		}
		
		if (fieldNameSequence.size() == 1) {
			return field.getGenericType();
		} else {
			return getTypeInClass(fieldNameSequence.subList(1, fieldNameSequence.size()), field.getType());
		}
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
			if (!RoleAnnotationsHelper.isPublicAndNonstatic(field)) {
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
	 * @throws AnnotationCheckerException
	 */
	private void checkRoleFieldsImplementation(List<Field> knowledgeFields, Class<?> roleClass) throws AnnotationCheckerException {
		List<Field> roleFields = getNonLocalKnowledgeFields(roleClass, true);
		
		for (Field roleField : roleFields) {
			if (!isRoleFieldImplemented(knowledgeFields, roleField.getGenericType(), roleField.getName())) {
				throw new AnnotationCheckerException("The field " + roleClass.getSimpleName() + "."
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
	private boolean isRoleFieldImplemented(List<Field> knowledgeFields, Type roleFieldType, String roleFieldName) {
		for (Field knowledgeField : knowledgeFields) {
			if (knowledgeField.getName().equals(roleFieldName)) {
				Type knowledgeFieldType = knowledgeField.getGenericType();
				return compareTypes(knowledgeFieldType, roleFieldType);
			}
		}
		
		return false; // no field with equal name
	}
	
	private boolean compareTypes(Type implementationType, Type roleType) {
		if (implementationType.equals(roleType)) {
			return true;
		}
		
		// nonequal types can be equal, if one of them is a generic type (or generically parametrized)
		if (implementationType instanceof GenericArrayType && roleType instanceof GenericArrayType) {
			GenericArrayType gType1 = (GenericArrayType) implementationType;
			GenericArrayType gType2 = (GenericArrayType) roleType;
			return compareTypes(gType1.getGenericComponentType(), gType2.getGenericComponentType());
		
		} else if (implementationType instanceof ParameterizedType && roleType instanceof ParameterizedType) {
			ParameterizedType pType1 = (ParameterizedType) implementationType;
			ParameterizedType pType2 = (ParameterizedType) roleType;
			if (!compareTypes(pType1.getRawType(), pType2.getRawType())) {
				return false;
			}
			
			assert pType1.getActualTypeArguments().length == pType2.getActualTypeArguments().length;
			for (int i = 0; i < pType1.getActualTypeArguments().length; i++) {
				if (!compareTypes(pType1.getActualTypeArguments()[i], pType2.getActualTypeArguments()[i])) {
					return false;
				}
			}
			
			return true;
		
		} else if (implementationType instanceof TypeVariable || roleType instanceof TypeVariable) {
			return true;
		}
		
		return false;
	}
	
}
