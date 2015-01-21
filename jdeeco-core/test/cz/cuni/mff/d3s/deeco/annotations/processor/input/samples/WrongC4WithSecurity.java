package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import java.util.Date;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.HasRole;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.RoleParam;

/**
 * the ID field is marked as secured
 * @author Ondřej Štumpf
 */

@Component
@HasRole(WrongC4WithSecurity.Role1.class)
@HasRole(WrongC4WithSecurity.Role2.class)
public class WrongC4WithSecurity {

	@RoleDefinition
	public static interface Role1 {
		
	}
	
	@RoleDefinition
	public static interface Role2 {
		@RoleParam
		public static final String param1 = "a";
		@RoleParam
		public static final String param2 = null;
	}
	
	@RoleDefinition
	public static interface Role3 {
		
	}
	
	public String name;
	
	@Allow(WrongC4WithSecurity.Role1.class)
	@Allow(WrongC4WithSecurity.Role3.class)
	public Integer capacity;
	
	@Allow(WrongC4WithSecurity.Role2.class)
	public Date time;
	
	@Allow(WrongC4WithSecurity.Role1.class)
	public String id;
}
