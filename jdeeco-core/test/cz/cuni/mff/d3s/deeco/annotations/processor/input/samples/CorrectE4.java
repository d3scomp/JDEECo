package cz.cuni.mff.d3s.deeco.annotations.processor.input.samples;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Rating;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.integrity.ReadonlyRatingsHolder;

/**
 * @author Ondřej Štumpf
 * 
 */
@Ensemble
public class CorrectE4 {

	@Membership
	public static boolean process1(
			@In("member.[coord.id]") String id,
			@Rating("member.[coord.id]") ReadonlyRatingsHolder idRating,
			@In("member.name") @TriggerOnChange String name) {
		return true;
	}

	@KnowledgeExchange
	public static void process2(
			@InOut("coord.details.[member.data.[member.id]].date") String date,
			@Rating("coord.details.[member.data.[member.id]].date") ReadonlyRatingsHolder dateRating,
			@In("member.id") @TriggerOnChange Integer plcsId) {
		
	}


}
