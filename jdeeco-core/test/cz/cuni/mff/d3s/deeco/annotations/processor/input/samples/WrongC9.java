package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.HasSecurityRole;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.SecurityRoleDefinition;

/**
 * @author Ondřej Štumpf
 */

@Component
@HasSecurityRole(WrongC9.Role1.class)
public class WrongC9 {

	@SecurityRoleDefinition
	public static interface Role1 {
		
	}
	
	// local knowledge cannot be secured
	@Allow(Role1.class)
	@Local
	public Integer capacity;
	
}
