package cz.cuni.mff.d3s.jdeeco.turtlebot.simpleexchange;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.DockingIRSignal;

@Ensemble
@PeriodicScheduling(period = 500, offset = 60)
public class DockEnsemble {

	@Membership
	public static boolean membership(@In("coord.receiverId") String receiverId,
			@In("member.sensingId") String sensingId) {
		return true;
	}

	@KnowledgeExchange
	public static void bumperExchange(
			@In("member.dockLeft") DockingIRSignal memberValueL,
			@In("member.dockCenter") DockingIRSignal memberValueC,
			@In("member.dockRight") DockingIRSignal memberValueR,
			@Out("coord.dockLeft") ParamHolder<DockingIRSignal> coordFieldL,
			@Out("coord.dockCenter") ParamHolder<DockingIRSignal> coordFieldC,
			@Out("coord.dockRight") ParamHolder<DockingIRSignal> coordFieldR) {
		coordFieldL.value = memberValueL;
		coordFieldC.value = memberValueC;
		coordFieldR.value = memberValueR;
	}
}
