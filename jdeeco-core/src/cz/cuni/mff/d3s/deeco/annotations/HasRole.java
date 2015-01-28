package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Used to decorate a component with desired roles. The parameter
 * is the security interface, i.e. interface decorated with {@link RoleDefinition} with possible parameters {@link RoleParam}. 
 * This annotation can be used multiple times on the same component.
 * 
 * @author Ondřej Štumpf
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(value=HasRoleMultiple.class)
public @interface HasRole {
	Class<?> value();
}
