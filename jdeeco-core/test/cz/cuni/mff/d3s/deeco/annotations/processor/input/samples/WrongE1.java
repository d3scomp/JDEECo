package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;

/**
 * Method marked as <code>@{@link Membership}</code>/
 * <code>@{@link KnowledgeExchange}</code> is not public and static.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
@Ensemble
@PeriodicScheduling(period=3000)
public class WrongE1 {

	@Membership
	public static boolean process1(
			@In("member.id") String id,
			@In("member.name") String name) {
		return true;
	}

	@KnowledgeExchange
	static void knowledgeExchange(
			@InOut("coord.details.date") @TriggerOnChange String date,
			@In("member.id") Integer plcsId) {
		// knowledge exchange logic
	}

}
