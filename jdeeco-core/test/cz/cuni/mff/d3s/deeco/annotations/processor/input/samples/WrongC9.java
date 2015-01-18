package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.HasRole;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;

/**
 * @author Ondřej Štumpf
 */

@Component
@HasRole(roleClass = WrongC9.Role1.class)
public class WrongC9 {

	@RoleDefinition
	public static interface Role1 {
		
	}
	
	// local knowledge cannot be secured
	@Allow(roleClass = Role1.class)
	@Local
	public Integer capacity;
	
}
