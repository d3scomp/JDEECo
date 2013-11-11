package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;

/**
 * Invalid path ecpression. 
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
@Component
public class WrongKP2 {

	@Process @PeriodicScheduling(1000)
	public static void process1(
			@InOut("level1. .level2") String p2) {
		// business logic
	}

}