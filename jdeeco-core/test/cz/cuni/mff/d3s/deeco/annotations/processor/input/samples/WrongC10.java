package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.HasRole;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;

/**
 * @author Ondřej Štumpf
 */

@Component
@HasRole(WrongC10.Role1.class)
public class WrongC10 {

	// aliasBy refers to something what is not a role definition
	@RoleDefinition(aliasedBy = WrongC10.class)
	public static interface Role1 {
		
	}
	
	
	@Allow(Role1.class)
	public Integer capacity;
	
}
