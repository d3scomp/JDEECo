package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import java.util.Date;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.HasSecurityRole;
import cz.cuni.mff.d3s.deeco.annotations.SecurityRoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.SecurityRoleParam;

/**
 * the ID field is marked as secured
 * @author Ondřej Štumpf
 */

@Component
@HasSecurityRole(WrongC4WithSecurity.Role1.class)
@HasSecurityRole(WrongC4WithSecurity.Role2.class)
public class WrongC4WithSecurity {

	@SecurityRoleDefinition
	public static interface Role1 {
		
	}
	
	@SecurityRoleDefinition
	public static interface Role2 {
		@SecurityRoleParam
		public static final String param1 = "a";
		@SecurityRoleParam
		public static final String param2 = null;
	}
	
	@SecurityRoleDefinition
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
