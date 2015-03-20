package example2;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@PeriodicScheduling(period = 1000)
public class FollowerLeaderEnsemble {

	public static final double ENSEMBLE_RADIUS = 2000.0;

	@Membership
	public static boolean membership(
			@In("member.id") String mId,
			@In("coord.id") String cId,
			@In("member.position") Coord mPos,
			@In("coord.position") Coord cPos) {

		return getEuclidDistance(cPos, mPos) <= ENSEMBLE_RADIUS && cId.compareTo(mId) < 0;
	}

	@KnowledgeExchange
	public static void exchange(
			@In("coord.destinationLink") Id cDestinationLink,
			@Out("member.leaderDestinationLink") ParamHolder<Id> mLeaderDestinationLink) {

		mLeaderDestinationLink.value = cDestinationLink;
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
