package cz.cuni.mff.d3s.jdeeco.turtlebot.simpleexchange;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.WheelState;

@Ensemble
@PeriodicScheduling(period = 500, offset = 85)
public class WheelEnsemble {

	@Membership
	public static boolean membership(@In("coord.receiverId") String receiverId,
			@In("member.sensingId") String sensingId) {
		return true;
	}

	@KnowledgeExchange
	public static void bumperExchange(
			@In("member.wheelLeft") WheelState memberValue0,
			@In("member.wheelRight") WheelState memberValue1,
			@Out("coord.wheelLeft") ParamHolder<WheelState> coordField0,
			@Out("coord.wheelRight") ParamHolder<WheelState> coordField1) {
		coordField0.value = memberValue0;
		coordField1.value = memberValue1;
	}
}
