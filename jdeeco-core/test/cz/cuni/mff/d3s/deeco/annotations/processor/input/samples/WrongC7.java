package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.Rating;
import cz.cuni.mff.d3s.deeco.annotations.RatingsProcess;
import cz.cuni.mff.d3s.deeco.integrity.RatingsHolder;
import cz.cuni.mff.d3s.deeco.integrity.ReadonlyRatingsHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * @author Ondřej Štumpf
 */

@Component
public class WrongC7 {

	// component process contains modifiable RatingsHolder
	@Process
	@PeriodicScheduling(period=1000)
	public static void process1(@In("field1") Integer field1, @Rating("field2") RatingsHolder field2, @Out("field3") ParamHolder<Integer> field3) {
		
	}
	
	@RatingsProcess
	public static void ratingsProcess(@In("field1") Integer field1, @Rating("field2") ReadonlyRatingsHolder field2, @Rating("field3") RatingsHolder field3) {
		
	}
}
