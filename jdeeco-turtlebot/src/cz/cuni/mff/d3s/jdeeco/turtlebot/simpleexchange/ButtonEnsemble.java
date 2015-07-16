package cz.cuni.mff.d3s.jdeeco.turtlebot.simpleexchange;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.ButtonState;


@Ensemble
@PeriodicScheduling(period = 500, offset = 110)
public class ButtonEnsemble {

	@Membership
	public static boolean membership(@In("coord.receiverId") String receiverId,
			@In("member.sensingId") String sensingId) {
		return true;
	}

	@KnowledgeExchange
	public static void bumperExchange(
			@In("member.button0") ButtonState memberValue0,
			@In("member.button1") ButtonState memberValue1,
			@In("member.button2") ButtonState memberValue2,
			@Out("coord.button0") ParamHolder<ButtonState> coordField0,
			@Out("coord.button1") ParamHolder<ButtonState> coordField1,
			@Out("coord.button2") ParamHolder<ButtonState> coordField2) {
		coordField0.value = memberValue0;
		coordField1.value = memberValue1;
		coordField2.value = memberValue2;
	}
}
