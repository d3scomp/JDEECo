package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.RoleParam;

/**
 * @author Ondřej Štumpf
 */

@Component
public class CorrectC6 {

	@RoleDefinition
	public static interface Role0 {
		@RoleParam
		public static final String fieldRole0 = "[0]";
	}
	
	@RoleDefinition
	public static interface Role1 extends Role0 {
		@RoleParam
		public static final String fieldRole0 = null;
	}	
	
	@Allow(roleClass = Role1.class)
	public String name;
}
