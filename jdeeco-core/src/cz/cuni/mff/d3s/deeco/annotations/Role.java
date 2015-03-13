package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a particular class as a role definition. Can be applied only to classes (not interfaces).
 * The annotated class should contain only public fields.
 * Components can implement the role using the {@link PlaysRole} attribute.
 * Components implementing the role must contain all fields specified in the role definition class.
 * 
 * @author Zbyněk Jiráček
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
// TODO rename to RoleDefinition once the current RoleDefinition annotation is renamed to SecurityRoleDefinition
public @interface Role {

}
