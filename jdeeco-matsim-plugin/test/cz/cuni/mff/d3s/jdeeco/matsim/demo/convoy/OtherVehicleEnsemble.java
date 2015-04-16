package cz.cuni.mff.d3s.jdeeco.matsim.demo.convoy;


import org.matsim.api.core.v01.Id;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;


@Ensemble
@PeriodicScheduling(period=500)
public class OtherVehicleEnsemble {

	@Membership
	public static boolean membership(
			@In("member.id") String memberId,
			@In("coord.id") String coordId) {
//		System.out.println("membership: "+ memberId + " " + coordId);
		return !(memberId.equals(coordId));
	}

	@KnowledgeExchange
	public static void map(
			@In("member.currentLink") Id memberLink,
			@Out("coord.otherVehicleLink") ParamHolder<Id> coordOtherLink,
			@In("member.id") String memberId,
			@In("coord.id") String coordId) {
//		System.out.println("exchange: "+ memberId + " " + coordId);
		coordOtherLink.value = memberLink;
	}
}
