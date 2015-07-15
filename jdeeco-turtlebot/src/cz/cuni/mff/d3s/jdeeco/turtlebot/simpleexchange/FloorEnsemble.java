package cz.cuni.mff.d3s.jdeeco.turtlebot.simpleexchange;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.FloorSensorState;

@Ensemble
@PeriodicScheduling(period = 500, offset = 65)
public class FloorEnsemble {

@Membership
public static boolean membership(@In("coord.receiverId") String receiverId,
		@In("member.sensingId") String sensingId) {
	return true;
}

@KnowledgeExchange
public static void bumperExchange(
		@In("member.floorLeft") FloorSensorState memberValueL,
		@In("member.floorCenter") FloorSensorState memberValueC,
		@In("member.floorRight") FloorSensorState memberValueR,
		@Out("coord.floorLeft") ParamHolder<FloorSensorState> coordFieldL,
		@Out("coord.floorCenter") ParamHolder<FloorSensorState> coordFieldC,
		@Out("coord.floorRight") ParamHolder<FloorSensorState> coordFieldR) {
	coordFieldL.value = memberValueL;
	coordFieldC.value = memberValueC;
	coordFieldR.value = memberValueR;
}
}
