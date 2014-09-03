package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;

/**
 * More than one annotation for the same parameter.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
@Component
public class WrongC3 {

	@Process @PeriodicScheduling(period=1000)
	public static void process1(
			@In("level1") Integer p1,
			@InOut("level1") String p2,
			@Out("alevel1") @In("aSecondlevel1") Boolean p3) {
		// business code
	}

}
