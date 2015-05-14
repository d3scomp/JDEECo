package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Used to decorate a knowledge field with access restrictions. The parameter
 * is the security interface, i.e. interface decorated with {@link SecurityRoleDefinition} with possible parameters {@link SecurityRoleParam}. 
 * This annotation can be used multiple times on the same field.
 * @author Ondřej Štumpf
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(value=AllowMultiple.class)
public @interface Allow {
	Class<?> value();
}
