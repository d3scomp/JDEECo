package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import java.util.Date;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.HasSecurityRole;
import cz.cuni.mff.d3s.deeco.annotations.SecurityRoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.SecurityRoleParam;

/**
 * @author Ondřej Štumpf
 */

@Component
@HasSecurityRole(CorrectC4.Role1.class)
@HasSecurityRole(CorrectC4.Role2.class)
public class CorrectC4 {

	@SecurityRoleDefinition(aliasedBy = Role3.class)
	public static interface Role1 {
		@SecurityRoleParam
		public static final String x = null;
	}
	
	@SecurityRoleDefinition
	public static interface Role2 {
		@SecurityRoleParam
		public static final String name = "[name]";
		@SecurityRoleParam
		public static final String time = null;
		@SecurityRoleParam
		public static final Integer x_integer = 123;
		@SecurityRoleParam
		public static final String x_string = "some_value";
		@SecurityRoleParam
		public static final String[] x_array = {"a", "b", "c"};
	}
	
	@SecurityRoleDefinition
	public static interface Role3 {
		
	}
	
	public String name;
	
	@Allow(Role1.class)
	@Allow(Role3.class)
	public Integer capacity;
	
	@Allow(Role2.class)
	public Date time;
}
