package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import cz.cuni.mff.d3s.deeco.annotations.CoordinatorRole;
import cz.cuni.mff.d3s.deeco.annotations.MemberRole;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Role;

/**
 * Helper methods for Annotations that are connected to component roles.
 * Because Java is stupid and does not allow annotation inheritance, some methods need to be duplicated.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class RoleAnnotationsHelper {

	public static Class<?>[] getCoordinatorRoleAnnotations(Class<?> clazz) throws AnnotationCheckerException {
		Class<?>[] result = translateCoordinatorRoles(clazz.getAnnotationsByType(CoordinatorRole.class));
		checkRolesAreValid(result);
		return result;
	}

	public static Class<?>[] getMemberRoleAnnotations(Class<?> clazz) throws AnnotationCheckerException {
		Class<?>[] result = translateMemberRoles(clazz.getAnnotationsByType(MemberRole.class));
		checkRolesAreValid(result);
		return result;
	}

	public static Class<?>[] getPlaysRoleAnnotations(Class<?> clazz) throws AnnotationCheckerException {
		Class<?>[] result = translatePlaysRole(clazz.getAnnotationsByType(PlaysRole.class));
		checkRolesAreValid(result);
		return result;
	}
	
	public static Class<?>[] translateCoordinatorRoles(CoordinatorRole[] coordinatorRoles) {
		Class<?>[] result = new Class<?>[coordinatorRoles.length];
		for (int i = 0; i < coordinatorRoles.length; i++) {
			result[i] = coordinatorRoles[i].value();
		}
		
		return result;
	}

	static Class<?>[] translateMemberRoles(MemberRole[] memberRoles) {
		Class<?>[] result = new Class<?>[memberRoles.length];
		for (int i = 0; i < memberRoles.length; i++) {
			result[i] = memberRoles[i].value();
		}
		
		return result;
	}

	static Class<?>[] translatePlaysRole(PlaysRole[] roles) {
		Class<?>[] result = new Class<?>[roles.length];
		for (int i = 0; i < roles.length; i++) {
			result[i] = roles[i].value();
		}
		
		return result;
	}
	
	static void checkRolesAreValid(Class<?>[] roles) throws AnnotationCheckerException {
		for (Class<?> role : roles) {
			checkRoleIsValid(role);
		}
	}
	
	static void checkRoleIsValid(Class<?> roleClass) throws AnnotationCheckerException {
		if (roleClass.getAnnotation(Role.class) == null) {
			throw new AnnotationCheckerException("The class " + roleClass.getSimpleName() + " is used as a role class, but it is not annotated by the @" + Role.class.getSimpleName() + " annotation.");
		}
	}

	static boolean isPublicAndNonstatic(Field field) {
		return !Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers());
	}
	
	/**
	 * Some java black magic.
	 * 
	 * If it starts with the string 'class ' or if it contains dots, or if it is a primitive type,
	 * then the type actually exist. Otherwise it's just a type parameter name.
	 * 
	 * @return True, if the given type is not an actual type, but only a type parameter
	 */
	static boolean isTypeParameterName(Type type) {
		String typeString = type.toString();
		if (typeString.startsWith("class ") || typeString.indexOf('.') >= 0) {
			return false;
		}
		
		if (typeString.equals("byte") || typeString.equals("short") || typeString.equals("int") 
				|| typeString.equals("long") || typeString.equals("float") || typeString.equals("double") 
				|| typeString.equals("char") || typeString.equals("boolean")) {
			return false;
		}
		
		return true;
	}

}
