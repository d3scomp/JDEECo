package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the annotated component class implements a particular role.
 * Roles can be defined using the {@link Role} attribute.
 * A component implements the role if it contains all fields that are specified in the role definition class.
 * 
 * @author Zbyněk Jiráček
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PlaysRole {
	Class<?> value();
}
