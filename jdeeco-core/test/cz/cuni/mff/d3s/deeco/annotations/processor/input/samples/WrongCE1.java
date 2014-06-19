package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.Component;

/**
 * No <code>@{@link Component}</code> annotation present.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
@PeriodicScheduling(period=1000)
public class WrongCE1 {

	@Process
	public static void process1(
			@In("level1") Integer p1,
			@InOut("level1.level2.level3") String p2,
			@Out("level1") Boolean p3) {
		// business code
	}

}
