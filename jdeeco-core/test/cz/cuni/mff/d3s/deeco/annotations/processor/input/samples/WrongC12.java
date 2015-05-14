package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SecurityRoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.SecurityRoleParam;

/**
 * @author Ondřej Štumpf
 */

@Component
public class WrongC12 {
	
	@SecurityRoleDefinition
	public static interface Role1 {
		@SecurityRoleParam
		public static String nonexisting_param = "[no_such_parameter]";
	}
	
	@Allow(Role1.class)
	public Integer securedCapacity;
	
	public Integer capacity;
	
	@Process
	@PeriodicScheduling(period = 1000)
	public static void process1(@InOut("capacity") Integer capacity) {
		
	}
}
