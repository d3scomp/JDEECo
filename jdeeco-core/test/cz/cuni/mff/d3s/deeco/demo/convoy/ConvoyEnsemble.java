package cz.cuni.mff.d3s.deeco.demo.convoy;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;


/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
@Ensemble
@PeriodicScheduling(period=100)
public class ConvoyEnsemble {

	@Membership
	public static boolean membership(
			@In("member.id") String memberId,
			@In("coord.id") String coordId,
			@In("member.position") Waypoint fPosition,
			@In("member.destination") Waypoint fDestination,
			@In("coord.position") Waypoint lPosition,
			@In("coord.path") List<Waypoint> lPath) {
		
		return 
			!fPosition.equals(fDestination) &&
			(Math.abs(lPosition.x - fPosition.x) + Math.abs(lPosition.y - fPosition.y)) <= 2 &&
			lPath.contains(fDestination);
	}

	@KnowledgeExchange
	public static void map(
			@In("member.id") String memberId,
			@In("coord.id") String coordId,
			@Out("member.leaderPosition") ParamHolder<Waypoint> fLeaderPosition,
			@In("coord.position") Waypoint lPosition) {
		
		fLeaderPosition.value = lPosition;
	}
}
