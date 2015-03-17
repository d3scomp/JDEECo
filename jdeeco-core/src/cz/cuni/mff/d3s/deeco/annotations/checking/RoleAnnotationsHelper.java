package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
	
	public static Class<?>[] getCoordinatorRoleAnnotations(Class<?> clazz) {
		Class<?>[] result = translateCoordinatorRoles(clazz.getAnnotationsByType(CoordinatorRole.class));
		return result;
	}

	public static Class<?>[] getMemberRoleAnnotations(Class<?> clazz) {
		Class<?>[] result = translateMemberRoles(clazz.getAnnotationsByType(MemberRole.class));
		return result;
	}

	public static Class<?>[] getPlaysRoleAnnotations(Class<?> clazz) {
		Class<?>[] result = translatePlaysRole(clazz.getAnnotationsByType(PlaysRole.class));
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
	
	// valid = has @Role annotation
	static void checkRolesAreValid(Class<?>[] roles) throws AnnotationCheckerException {
		for (Class<?> role : roles) {
			checkRoleIsValid(role);
		}
	}
	
	// valid = has @Role annotation
	static void checkRoleIsValid(Class<?> roleClass) throws AnnotationCheckerException {
		if (roleClass.getAnnotation(Role.class) == null) {
			throw new AnnotationCheckerException("The class " + roleClass.getSimpleName() + " is used as a role class, but it is not annotated by the @" + Role.class.getSimpleName() + " annotation.");
		}
	}

	static boolean isPublicAndNonstatic(Field field) {
		return !Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers());
	}

}
