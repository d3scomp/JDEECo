package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Used to mark an interface as a security role. 
 * Only such interfaces can be used as arguments for {@link Allow} and {@link HasRole} annotations.
 * 
 * @author Ondřej Štumpf
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RoleDefinition {
	Class<?> aliasedBy() default DEFAULT_ALIAS.class;
	
	static final class DEFAULT_ALIAS {}
}
