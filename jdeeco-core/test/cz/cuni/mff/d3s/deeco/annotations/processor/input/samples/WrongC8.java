package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.HasRole;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;

/**
 * @author Ondřej Štumpf
 */

@Component
@HasRole(roleClass = WrongC8.Role1.class)
public class WrongC8 {

	@RoleDefinition
	public static interface Role1 {
		
	}
	
	// the same role assigned multiply
	@Allow(roleClass = Role1.class)
	@Allow(roleClass = Role1.class)
	public Integer capacity;
	
}
