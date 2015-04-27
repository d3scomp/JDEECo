package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
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
import cz.cuni.mff.d3s.deeco.annotations.checking.ParameterKnowledgePathExtractor.KnowledgePathAndType;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
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
 * {@link RolesAnnotationChecker#checkComponentRolesImplementation(Object)} method.
 * 
 * 3) In ensembles where coordinator and member role is specified, knowledge paths
 * that are used in the membership and knowledge exchange methods are checked to be valid.
 * This means that the knowledge path must exist in the given role definition.
 * For details, see the {@link RolesAnnotationChecker#validateEnsemble(Class, EnsembleDefinition)}
 * and {@link RolesAnnotationChecker#checkEnsembleMethodRolesImplementation(List, Class[], Class[])} methods.
 * 
 * When checking the presence of a particular field, besides the field name, the type of the 
 * field must match to the type specified in the role class. The type comparisons are delegated
 * to {@link TypeComparer}.

 * @author Zbyněk Jiráček
 *
 */
public class RolesAnnotationChecker implements AnnotationChecker {
	
	private KnowledgePathChecker knowledgePathChecker;
	private TypeComparer typeComparer;
	private ParameterKnowledgePathExtractor parameterExtractor;
	
	public RolesAnnotationChecker(KnowledgePathChecker knowledgePathChecker, TypeComparer typeComparer) {
		this(knowledgePathChecker, typeComparer, new ParameterKnowledgePathExtractor());
	}
	
	// used just for tests - to be able to put a mock of ParameterKnowledgePathExtractor
	RolesAnnotationChecker(KnowledgePathChecker knowledgePathChecker, TypeComparer typeComparer,
			ParameterKnowledgePathExtractor parameterExtractor) {
		this.knowledgePathChecker = knowledgePathChecker;
		this.typeComparer = typeComparer;
		this.parameterExtractor = parameterExtractor;
	}
	
	/*
	 * (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationChecker#validateComponent(java.lang.Object, cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance)
	 */
	public void validateComponent(Object componentObj, ComponentInstance componentInstance) throws AnnotationCheckerException {		
		checkComponentRolesImplementation(componentObj);
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
		
		checkEnsembleMethodRolesImplementation(ensembleDefinition.getMembership().getParameters(), coordinatorRoles, memberRoles);
		checkEnsembleMethodRolesImplementation(ensembleDefinition.getKnowledgeExchange().getParameters(), coordinatorRoles, memberRoles);
	}
	
	/**
	 * Checks that a component instance class correctly implements all roles that are declared
	 * by the {@link PlaysRole} annotation. The function does not return any value, it either
	 * succeeds or throws an exception. A role is correctly implemented if all public fields
	 * from the role class are present in the component class and have same types. 
	 * @param obj The componentInstance
	 * @throws AnnotationCheckerException 
	 */
	void checkComponentRolesImplementation(Object obj) throws AnnotationCheckerException {
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
	void checkEnsembleMethodRolesImplementation(List<Parameter> parameters, Class<?>[] coordinatorRoles, 
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
		
		int i = 0;
		for (Parameter parameter : parameters) {
			i++;
			try {
				checkParameter(parameter, coordinatorRoles, memberRoles);
			} catch (ParameterException e) {
				throw new AnnotationCheckerException("Parameter " + i + ": " + e.getMessage(), e);
			}
		}
		
	}
	
	void checkParameter(Parameter parameter, Class<?>[] coordinatorRoles, Class<?>[] memberRoles) throws ParameterException {
		List<KnowledgePathAndType> knowledgePaths = parameterExtractor.extractAllKnowledgePaths(parameter);
		for (KnowledgePathAndType knowledgePathAndType : knowledgePaths) {
			try {
				checkKnowledgePath(knowledgePathAndType.type, knowledgePathAndType.knowledgePath,
						coordinatorRoles, memberRoles);
			} catch (KnowledgePathCheckException ex) {
				String knowledgePathStr = KnowledgePathCheckerImpl.pathNodeSequenceToString(
						knowledgePathAndType.type, knowledgePathAndType.knowledgePath);
				throw new ParameterException("Knowledge path " + knowledgePathStr + ": " 
						+ ex.getMessage(), ex);
			}
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
	private void checkKnowledgePath(Type type, List<PathNode> pathNodes, Class<?>[] coordinatorRoles, 
			Class<?>[] memberRoles) throws KnowledgePathCheckException {
		if (pathNodes.size() < 2) {
			throw new KnowledgePathCheckException("The knowledge path must contain at least two elements (coord/member and field name).");
		}
		
		// just choose coordinator / member role classes
		Class<?>[] roleClasses;
		PathNode first = pathNodes.get(0);
		if (first instanceof PathNodeCoordinator) {
			roleClasses = coordinatorRoles;
		} else if (first instanceof PathNodeMember) {
			roleClasses = memberRoles;
		} else {
			throw new KnowledgePathCheckException("The knowledge path does not start with coord/member.");
		}

		// Test the knowledge path against all roles
		// It is sufficient that the field belongs to at least one of the roles
		boolean satisfiesAnyRole = false;
		for (Class<?> roleClass : roleClasses) {
			if (knowledgePathChecker.isFieldInClass(type, pathNodes.subList(1, pathNodes.size()), roleClass)) {
				satisfiesAnyRole = true;
				break; // one role is enough
			}
		}
		
		String roleClassNames = String.join(", ", 
				Arrays.asList(roleClasses).stream().map(r -> r.getSimpleName()).collect(Collectors.toList()));
		
		if (!satisfiesAnyRole && roleClasses.length > 0) {
			throw new KnowledgePathCheckException("The knowledge path is not valid for any of the roles: " + roleClassNames + ". "
					+ "Check whether the field (or sequence of fields) exists in the role and that it has correct type(s) and is public, nonstatic and non@Local");
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
				return typeComparer.compareTypes(knowledgeFieldType, roleFieldType);
			}
		}
		
		return false; // no field with equal name
	}
	
}
