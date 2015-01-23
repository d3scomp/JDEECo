package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ContextKind;


/**
 * Used to mark a field within a security interface (interface decorated with {@link RoleDefinition} as a role parameter. 
 * Such field must be static and final.
 * The field can have following values:
 *  - null, then {@link BlankSecurityRoleArgument} is created. This signals "any value is allowed".
 *  - String in the format "[knowledge.path]" (including the brackets). The {@link PathSecurityRoleArgument}, which signals that the value of knowledge.path is used as a value of the parameter.
 *  - any other object (including String not in the format above). Then {@link AbsoluteSecurityRoleArgument} with the object value is created.
 * The optional ContextKind specifies whether the argument value should be evaluated in the context of the local component, or in the
 * context of the replica from which the knowledge was accepted. This makes difference only for {@link PathSecurityRoleArgument}.
 * 
 * @author Ondřej Štumpf
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RoleParam {
	ContextKind value() default ContextKind.LOCAL;
}
