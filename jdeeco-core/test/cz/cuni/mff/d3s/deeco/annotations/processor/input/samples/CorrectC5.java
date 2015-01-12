package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.RoleParam;

/**
 * @author Ondřej Štumpf
 */

@Component
public class CorrectC5 {

	@RoleDefinition
	public static interface Role0 {
		@RoleParam
		public static final String fieldRole0 = "[0]";
	}
	
	@RoleDefinition
	public static interface Role1 extends Role0 {
		@RoleParam
		public static final String fieldRole1 = "[x]";
		
		@RoleParam
		public static final Integer fieldRole_date = 123;
	}
	
	@RoleDefinition
	public static interface Role2 {
		@RoleParam
		public static final String fieldRole2 = "[y]";		
	}
	
	@RoleDefinition
	public static interface Role3 extends Role1, Role2 {
		@RoleParam
		public static final String fieldRole2 = null;
		@RoleParam
		public static final String fieldRole3 = "[v]";
		@RoleParam
		public static final String fieldRole0 = "value_override";
	}
	
	@Allow(roleClass = Role3.class)
	public String name;
}
