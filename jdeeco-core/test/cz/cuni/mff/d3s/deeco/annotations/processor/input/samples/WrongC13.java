package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;

/**
 * @author Ondřej Štumpf
 */

@Component
public class WrongC13 {
	
	@RoleDefinition
	public static interface Role1 {
		
	}
	
	// Non-serializable objects cannot be secured
	@Allow(Role1.class)
	public NonSerializable nonserializable = new NonSerializable();

	public static class NonSerializable {
		
	}
}
