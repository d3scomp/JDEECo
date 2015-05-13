package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import java.util.Date;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.HasRole;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.RoleParam;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AccessRights;

/**
 * @author Ondřej Štumpf
 */

@Component
@HasRole(CorrectC4.Role1.class)
@HasRole(CorrectC4.Role2.class)
public class CorrectC4 {

	@RoleDefinition(aliasedBy = Role3.class)
	public static interface Role1 {
		@RoleParam
		public static final String x = null;
	}
	
	@RoleDefinition
	public static interface Role2 {
		@RoleParam
		public static final String name = "[name]";
		@RoleParam
		public static final String time = null;
		@RoleParam
		public static final Integer x_integer = 123;
		@RoleParam
		public static final String x_string = "some_value";
		@RoleParam
		public static final String[] x_array = {"a", "b", "c"};
	}
	
	@RoleDefinition
	public static interface Role3 {
		
	}
	
	public String name;
	
	@Allow(value = Role1.class, accessRights = AccessRights.READ)
	@Allow(value = Role3.class, accessRights = AccessRights.WRITE)
	public Integer capacity;
	
	@Allow(value = Role2.class, accessRights = AccessRights.READ_WRITE)
	public Date time;
}
