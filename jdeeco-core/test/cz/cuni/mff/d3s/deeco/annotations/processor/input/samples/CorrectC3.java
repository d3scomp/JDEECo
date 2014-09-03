package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;

/**
 * Tests parsing of Map entries in knowledge paths.
 *  
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
@Component
public class CorrectC3 {

	@Process @PeriodicScheduling(period=2000)
	public static void process1(
			@InOut("level1.[level21.level22].level3") String p2) {
		// business logic
	}

}
