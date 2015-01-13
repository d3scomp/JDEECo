package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import java.util.Date;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.HasRole;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.RoleParam;

/**
 * @author Ondřej Štumpf
 */

@Component
@HasRole(roleClass = CorrectC4.Role1.class)
@HasRole(roleClass = CorrectC4.Role2.class)
public class CorrectC4 {

	@RoleDefinition
	public static interface Role1 {
		
	}
	
	@RoleDefinition
	public static interface Role2 {
		@RoleParam
		public static final String name = "[name_path]";
		@RoleParam
		public static final String time = null;
		@RoleParam
		public static final Integer x_integer = 123;
		@RoleParam
		public static final String x_string = "some_value";
	}
	
	@RoleDefinition
	public static interface Role3 {
		
	}
	
	public String name;
	
	@Allow(roleClass = Role1.class)
	@Allow(roleClass = Role3.class)
	public Integer capacity;
	
	@Allow(roleClass = Role2.class)
	public Date time;
}
