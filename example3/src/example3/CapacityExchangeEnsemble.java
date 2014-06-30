package example3;

import java.util.Map;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import example3.VehicleComponent.LinkCapacityEntry;

@Ensemble
@PeriodicScheduling(period = 10000)
public class CapacityExchangeEnsemble {

	public static final double ENSEMBLE_RADIUS = 2000.0; // in meters

	@Membership
	public static boolean membership(
			@In("member.position") Coord mPos,
			@In("coord.position") Coord cPos) {

		return getEuclidDistance(cPos, mPos) <= ENSEMBLE_RADIUS;
	}

	@KnowledgeExchange
	public static void exchange(
			@In("coord.linksCapacity") Map<Id, LinkCapacityEntry> cLinksCapacity,
			@InOut("member.linksCapacity") ParamHolder<Map<Id, LinkCapacityEntry>> mLinksCapacity) {

		for (LinkCapacityEntry cEntry : cLinksCapacity.values()) {
			LinkCapacityEntry mEntry = mLinksCapacity.value.get(cEntry.linkId);
			if (mEntry == null || mEntry.timestamp < cEntry.timestamp) {
				mLinksCapacity.value.put(cEntry.linkId, cEntry);
			}
		}
	}

	private static double getEuclidDistance(Coord p1, Coord p2) {
		if (p1 == null || p2 == null) {
			return Double.POSITIVE_INFINITY;
		}

		double dx = p1.getX() - p2.getX();
		double dy = p1.getY() - p2.getY(); 

		return Math.sqrt(dx*dx + dy*dy);
	}
}
