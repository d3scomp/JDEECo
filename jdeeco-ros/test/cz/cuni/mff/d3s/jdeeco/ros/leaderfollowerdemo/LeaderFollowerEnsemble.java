package cz.cuni.mff.d3s.jdeeco.ros.leaderfollowerdemo;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.position.Position;

@Ensemble
@PeriodicScheduling(period = 100)
public class LeaderFollowerEnsemble {
	@Membership
	public static boolean membership(@In("coord.id") String coordId, @In("coord.position") Position coordPosition,
			@In("member.id") String memberId, @In("member.position") Position memberPosition) {
		Log.i(LeaderFollowerEnsemble.class.getName() + " MEMBERSHIP");
		return coordId.equals("robot_0") && memberId.equals("robot_1") && coordPosition.euclidDistanceTo(memberPosition) < 200;
	}
	
	@KnowledgeExchange
	public static void exchange(@In("coord.position") Position coordPosition, @Out("member.destination") ParamHolder<Position> memberPosition) {
		Log.i(LeaderFollowerEnsemble.class.getName() + " EXCHANGE");
		memberPosition.value = coordPosition;
	}
}
