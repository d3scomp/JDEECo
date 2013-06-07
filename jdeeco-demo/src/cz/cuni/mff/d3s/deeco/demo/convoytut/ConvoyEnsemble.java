package cz.cuni.mff.d3s.deeco.demo.convoytut;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;



public class ConvoyEnsemble extends Ensemble {

	@Membership
	public static boolean membership(
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
	@PeriodicScheduling(200)
	public static void map(
			@Out("member.leaderPosition") OutWrapper<Waypoint> fLeaderPosition,
			@In("coord.position") Waypoint lPosition) {
		
		fLeaderPosition.value = lPosition;
	}
}
