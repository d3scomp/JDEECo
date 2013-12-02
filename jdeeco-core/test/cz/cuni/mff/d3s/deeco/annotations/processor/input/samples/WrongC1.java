package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.Process;

/**
 * Method annotated as <code>@{@link Process}</code> is not public and static. 
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
@Component
public class WrongC1 {

	@Process
	public void process1(
			@In("level1") Integer p1,
			@InOut("level1.level2.level3") String p2,
			@Out("level1") Boolean p3) {
		// business code
	}

}
