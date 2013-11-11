package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;

/**
 * Just testing that all different annotations that can be used in defining a
 * component are correctly parsed.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
@Component
public class CorrectC1 {

	@Process
	@PeriodicScheduling(1000)
	public static void process1(
			@In("level1") @TriggerOnChange Integer p1,
			@InOut("level1.level2.level3") String p2,
			@Out("level1") Boolean p3) {
		// business logic
	}
	
	// should be ignored
	@PeriodicScheduling(2000)
	public static void helper(){
	}
	
	// should be ignored
	public static void helper2(@In("a") Integer ignoreMe) {
	}

	// should be ignored
	public static void helper2(@In("b") @TriggerOnChange String ignoreMe) {
	}
	
	// should be ignored
	public static void helper4() {
	}
	
	// should be ignored
	void helper3() {
	}
	
}
