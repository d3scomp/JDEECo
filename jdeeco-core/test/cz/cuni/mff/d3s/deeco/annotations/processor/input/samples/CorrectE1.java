package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;

/**
 * Just checking that annotations that can be used in an ensemble definition are
 * correctly parsed.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
@Ensemble
@PeriodicScheduling(3000)
public class CorrectE1 {

	@Membership
	public static boolean process1(
			@In("member.id") String id,
			@In("member.name") String name) {
		return true;
	}

	@KnowledgeExchange
	public static void process2(
			@InOut("coord.details.date") @TriggerOnChange String date,
			@In("member.id") Integer plcsId) {
		// knowledge exchange logic
	}

	// should be ignored
	public static void helper1() {

	}
	
	// should be ignored
	public void helper2() {

	}
	
	void helper3() {
		
	}

}
