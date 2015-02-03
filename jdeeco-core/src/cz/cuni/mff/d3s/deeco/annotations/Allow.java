package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cz.cuni.mff.d3s.deeco.model.runtime.api.AccessRights;


/**
 * Used to decorate a knowledge field with access restrictions. The value parameter
 * is the security interface, i.e. interface decorated with {@link RoleDefinition} with possible parameters {@link RoleParam}.
 * The accessRights parameter specifies in what kind of argument can the knowledge field appear (read->in, write->out, read_write -> inout). 
 * This annotation can be used multiple times on the same field.
 * @author Ondřej Štumpf
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(value=AllowMultiple.class)
public @interface Allow {
	Class<?> value();
	AccessRights accessRights() default AccessRights.READ_WRITE;
}
