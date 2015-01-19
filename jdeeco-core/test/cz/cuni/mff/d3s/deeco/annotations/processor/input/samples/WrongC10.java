package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.HasRole;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.RoleParam;

/**
 * @author Ondřej Štumpf
 */

@Component
@HasRole(roleClass = WrongC10.Role1.class)
public class WrongC10 {

	// role added to a component which cannot resolve the parameter knowledge path
	@RoleDefinition
	public static interface Role1 {
		@RoleParam
		public static final String param_nonexisting = "[no_such_field]";
	}
	
	public Integer capacity;
	
}
