package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
 * Every component can implement a number of roles specified by the {@link PlaysRole}
 * annotation. A role is a simple class with only public fields (others are ignored)
 * marked by the {@link Role} annotation. Roles work similarly to an interface 
 * (all the fields in the role need to be implemented in the component class).
 * This can be used also in ensembles, where the programmer can define {@link CoordinatorRole}
 * and {@link MemberRole}. This ensures, that the given ensemble can be formed
 * only between components that implement the specified roles.
 * 
 * This class is used to check that the roles are used correctly. This includes
 * following checks:
 * 
 * 1) All classes used as role classes must be annotated by the {@link Role} attribute.
 * See {@link RoleAnnotationsHelper} class that implements this.
 * 
 * 2) When a component implements a role, all public fields from this role need to be
 * present also in the component class. For details, see the
 * {@link RolesAnnotationChecker#checkRolesImplementation(Object)} method.
 * 
 * 3) In ensembles where coordinator and member role is specified, knowledge paths
 * that are used in the membership and knowledge exchange methods are checked to be valid.
 * This means that the knowledge path must exist in the given role definition.
 * For details, see the {@link RolesAnnotationChecker#validateEnsemble(Class, EnsembleDefinition)}
 * and {@link RolesAnnotationChecker#checkRolesImplementation(List, Class[], Class[])} methods.
 * 
 * When checking the presence of a particular field, besides the field name, the type of the 
 * field must match to the type specified in the role class. In this case, inheritance is not
 * taken into account (the types need to be equal). Generics are considered
 * (eg. field of type List<<String>> cannot by implemented by a field of type
 * List<<Integer>>).

 * @author Zbyněk Jiráček
 *
 */
public class RolesAnnotationChecker implements AnnotationChecker {
	
	/*
	 * (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationChecker#validateComponent(java.lang.Object, cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance)
	 */
	public void validateComponent(Object componentObj, ComponentInstance componentInstance) throws AnnotationCheckerException {		
		checkRolesImplementation(componentObj);
	}
	
	/*
	 * (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationChecker#validateEnsemble(java.lang.Class, cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition)
	 */
	public void validateEnsemble(Class<?> ensembleClass, EnsembleDefinition ensembleDefinition) throws AnnotationCheckerException {
		if (ensembleClass == null) {
			throw new AnnotationCheckerException("The input ensemble class cannot be null.");
		}
		if (ensembleDefinition == null) {
			throw new AnnotationCheckerException("The input ensemble definition cannot be null.");
		}
		
		Class<?>[] coordinatorRoles = RoleAnnotationsHelper.getCoordinatorRoleAnnotations(ensembleClass);
		RoleAnnotationsHelper.checkRolesAreValid(coordinatorRoles);
		Class<?>[] memberRoles = RoleAnnotationsHelper.getMemberRoleAnnotations(ensembleClass);
		RoleAnnotationsHelper.checkRolesAreValid(memberRoles);
		if (coordinatorRoles.length > 1 || memberRoles.length > 1) {
			throw new AnnotationCheckerException("Only one CoordinatorRole and one MemberRole annotation is allowed per ensemble.");
		}
		
		checkRolesImplementation(ensembleDefinition.getMembership().getParameters(), coordinatorRoles, memberRoles);
		checkRolesImplementation(ensembleDefinition.getKnowledgeExchange().getParameters(), coordinatorRoles, memberRoles);
	}
	
	/**
	 * Checks that a component instance class correctly implements all roles that are declared
	 * by the {@link PlaysRole} annotation. The function does not return any value, it either
	 * succeeds or throws an exception. A role is correctly implemented if all public fields
	 * from the role class are present in the component class and have same types. 
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
		RoleAnnotationsHelper.checkRolesAreValid(roleAnnotations);
		for (Class<?> roleClass : roleAnnotations) {			
			checkRoleFieldsImplementation(knowledgeFields, roleClass);
		}		
	}
	
	/**
	 * Checks that all of the given parameter's knowledge paths exist in given roles. Works for only for
	 * ensembles membership condition and knowledge exchange functions. The knowledge path is searched
	 * using introspection. The type of the expression is also considered and must match. However,
	 * some components and respective knowledge paths can be too complicated, only implication
	 * knowledge path valid => method succeeds is ensured. On the other hand, if the method succeeds,
	 * it does not necessarily mean that the knowledge path is valid. This can happen mostly with extensive
	 * use of generics.
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
	 * Checks that a given knowledge path is valid. Works only for ensembles membership
	 * condition and knowledge exchange functions.
	 * 
	 * A valid knowledge path must have at least two nodes (first being coord/member and second being
	 * a field name). More nodes can be present. A knowledge path must also exist in at least one
	 * role specified for the member/coordinator. The last condition does not apply if the role list
	 * for the member/coordinator is empty.
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

		// Test the knowledge path against all roles
		// It is sufficient that the field belongs to at least one of the roles
		boolean satisfiesAnyRole = false;
		for (Class<?> roleClass : roleClasses) {
			if (isFieldInRole(type, fieldNameSequence, roleClass)) {
				satisfiesAnyRole = true;
				break; // one role is enough
			}
		}
		
		String roleClassNames = String.join(", ", 
				Arrays.asList(roleClasses).stream().map(r -> r.getSimpleName()).collect(Collectors.toList()));
		
		if (!satisfiesAnyRole && roleClasses.length > 0) {
			throw new AnnotationCheckerException("The knowledge path '" + knowledgePath.toString() + "'"
					+ (type != null ? " of type " + type : "") + " is not valid for any of the roles: " + roleClassNames + ". "
					+ "Check whether the field (or sequence of fields) exists in the role and that it has correct type(s) and is public, nonstatic and non@Local");
		
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
	
	/**
	 * Returns the type of a field sequence from a given role class. If the given field sequence
	 * has only one element "id", String.class is returned (implicit field present in all roles).
	 * When the field sequence consist of multiple elements, the type of the nested field is returned
	 * (ie. if the given class contains a field x of type C, class C contains a field y and the 
	 * input field name sequence is ("x", "y"), then the result is the type of C.y).
	 * 
	 * If the given field has generic type, {@link ParameterizedType} is returned, containing
	 * the type arguments.
	 * 
	 * Sometimes generic types cannot be inferred. In that case it is possible, that an unknown
	 * type is returned. In this case the result is an instance of {@link TypeVariable}.
	 * 
	 * @param fieldNameSequence Sequence of fields (knowledge path split by dots)
	 * @param roleClass The role class
	 * @return Type of the expression.
	 */
	private Type getTypeInRole(List<String> fieldNameSequence, Class<?> roleClass) {
		if (fieldNameSequence.size() == 1 && fieldNameSequence.get(0).equals("id")) {
			// id is always present and always of type String
			return String.class;
		}
		
		return getTypeInClass(fieldNameSequence, roleClass);
	}
	
	/**
	 * Returns the type of a field sequence from a given class. When the field sequence consists
	 * of multiple elements, the type of the nested field is returned (ie. if the given class
	 * contains a field x of type C, class C contains a field y and the input field name
	 * sequence is ("x", "y"), then the result is the type of C.y).
	 * 
	 * If the given field has generic type, {@link ParameterizedType} is returned, containing
	 * the type arguments.
	 * 
	 * Sometimes generic types cannot be inferred. In that case it is possible, that an unknown
	 * type is returned. In this case the result is an instance of {@link TypeVariable}.
	 * 
	 * @param fieldNameSequence Sequence of fields (knowledge path split by dots)
	 * @param clazz The class
	 * @return Type of the expression.
	 */
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
	
	/**
	 * Checks whether a given types are (or can be) equal. This method is used to check whether
	 * a field of a given type can implement (or reference) a field of a given type from a role
	 * class. The method returns true, if the types are equal, or in all cases when the types
	 * cannot be proven to be different (this involves mostly unresolved generic arguments).
	 * Note that due to technical limitations, type hierarchy is not taken into account
	 * (ie. for classes A extends B, A and B are not considered equal in any case).
	 * @param implementationType The type of the field in the class/method
	 * @param roleType The type of the field as declared in the role
	 * @return True if types are equal/may be equal, false otherwise
	 */
	private boolean compareTypes(Type implementationType, Type roleType) {
		if (implementationType.equals(roleType)) {
			return true;
		}
		
		// nonequal types can be equal, if one of them is a generic type (or generically parameterized)
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
