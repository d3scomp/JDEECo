package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;

/**
 * Tests parsing of nested Map entries in knowledge paths.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
@Ensemble
public class CorrectE3 {

	@Membership
	public static boolean process1(
			@In("names.[coord.id]") String id,
			@In("member.name") @TriggerOnChange String name) {
		return true;
	}

	@KnowledgeExchange
	public static void process2(
			@InOut("coord.details.[member.data.[meta.id]].date") String date,
			@In("member.id") @TriggerOnChange Integer plcsId) {
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
