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

	public static final double ENSEMBLE_RADIUS = 2000.0;

	@Membership
	public static boolean membership(
			@In("coord.position") Coord cPos,
			@In("member.position") Coord mPos) {

		return getEuclidDistance(cPos, mPos) <= ENSEMBLE_RADIUS;
	}

	@KnowledgeExchange
	public static void exchange(
			@InOut("coord.linksCapacity") ParamHolder<Map<Id, LinkCapacityEntry>> cLinksCapacity,
			@In("member.linksCapacity") Map<Id, LinkCapacityEntry> mLinksCapacity) {

		for (LinkCapacityEntry mEntry : mLinksCapacity.values()) {
			LinkCapacityEntry cEntry = cLinksCapacity.value.get(mEntry.linkId);
			if (cEntry == null || cEntry.timestamp < mEntry.timestamp) {
				cLinksCapacity.value.put(mEntry.linkId, mEntry);
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
