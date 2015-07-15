package cz.cuni.mff.d3s.jdeeco.turtlebot.simpleexchange;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.BumperValue;

@Ensemble
@PeriodicScheduling(period = 500, offset = 50)
public class BumperEnsemble {
	@Membership
	public static boolean membership(@In("coord.receiverId") String receiverId,
			@In("member.sensingId") String sensingId) {
		return true;
	}

	@KnowledgeExchange
	public static void bumperExchange(
			@In("member.bumperValue") BumperValue memberValue,
			@Out("coord.bumperValue") ParamHolder<BumperValue> coordField) {
		coordField.value = memberValue;
	}
}
