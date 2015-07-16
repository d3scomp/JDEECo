package cz.cuni.mff.d3s.jdeeco.turtlebot.simpleexchange;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@PeriodicScheduling(period = 500, offset = 140)
public class InfoEnsemble {

	@Membership
	public static boolean membership(@In("coord.receiverId") String receiverId,
			@In("member.sensingId") String sensingId) {
		return true;
	}

	@KnowledgeExchange
	public static void bumperExchange(
			@In("member.fwInfo") String memberValue0,
			@In("member.swInfo") String memberValue1,
			@In("member.hwInfo") String memberValue2,
			@Out("coord.fwInfo") ParamHolder<String> coordField0,
			@Out("coord.swInfo") ParamHolder<String> coordField1,
			@Out("coord.hwInfo") ParamHolder<String> coordField2) {
		coordField0.value = memberValue0;
		coordField1.value = memberValue1;
		coordField2.value = memberValue2;
	}
}
