package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Usable only for membership methods in ensembles. Specifies that a particular membership
 * can be created only when the coordinator implements a given role.
 * 
 * Roles can be defined by the {@link Role} attribute.
 * Components can implement the role using the {@link PlaysRole} attribute.
 * 
 * @author Zbyněk Jiráček
 * @see MemberRole
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CoordinatorRole {
	Class<?> value();
}
