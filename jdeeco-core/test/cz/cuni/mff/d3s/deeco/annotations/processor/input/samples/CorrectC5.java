package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.SecurityRoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.SecurityRoleParam;

/**
 * @author Ondřej Štumpf
 */

@Component
public class CorrectC5 {

	@SecurityRoleDefinition
	public static interface Role0 {
		@SecurityRoleParam
		public static final String fieldRole0 = "[z]";
	}
	
	@SecurityRoleDefinition
	public static interface Role1 extends Role0 {
		@SecurityRoleParam
		public static final String fieldRole1 = "[x]";
		
		@SecurityRoleParam
		public static final Integer fieldRole_date = 123;
	}
	
	@SecurityRoleDefinition
	public static interface Role2 {
		@SecurityRoleParam
		public static final String fieldRole2 = "[y]";		
	}
	
	@SecurityRoleDefinition
	public static interface Role3 extends Role1, Role2 {
		@SecurityRoleParam
		public static final String fieldRole2 = null;
		@SecurityRoleParam
		public static final String fieldRole3 = "[v]";
		@SecurityRoleParam
		public static final String fieldRole0 = "value_override";
	}
	
	@Allow(Role3.class)
	public String name;
	
	public String x,y,z,v;
}
