package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;

@Component
public class ChildOfCorrectC1 extends CorrectC1 {

	public Integer number;
	
	@Process
	@PeriodicScheduling(2000)
	public static void myprocess1(
			@Out("x") Integer x) {
		// business logic
	}
	
}
