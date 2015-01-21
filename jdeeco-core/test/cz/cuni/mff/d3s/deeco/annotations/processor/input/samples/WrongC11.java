package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * @author Ondřej Štumpf
 */

@Component
public class WrongC11 {
	
	@RoleDefinition
	public static interface Role1 {
		
	}
	
	@Allow(roleClass = Role1.class)
	public Integer securedCapacity;
	
	public Integer capacity;
	
	// security compromise - knowledge being copied from secured field to a field without security
	@Process
	@PeriodicScheduling(period = 1000)
	public static void process1(@In("securedCapacity") Integer securedCapacity, @Out("capacity") ParamHolder<Integer> capacity) {
		
	}
}
