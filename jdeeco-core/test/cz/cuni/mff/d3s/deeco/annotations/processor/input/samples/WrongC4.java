package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.Process;

/**
 * No triggering annotations present.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
@Component
public class WrongC4 {

	@Process
	public static void process1(
			@In("level1") Integer p1,
			@InOut("level1.level2.level3") String p2,
			@Out("level1") Boolean p3) {
		// business logic
	}

}
