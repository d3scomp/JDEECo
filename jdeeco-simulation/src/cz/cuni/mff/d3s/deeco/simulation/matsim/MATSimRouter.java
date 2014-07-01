package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.api.experimental.facilities.Facility;
import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.router.TripRouter;
import org.matsim.core.router.TripRouterFactory;
import org.matsim.core.router.TripRouterFactoryImpl;
import org.matsim.core.router.util.DijkstraFactory;
import org.matsim.core.router.util.TravelTime;
import org.matsim.pt.router.TransitRouterConfig;
import org.matsim.pt.router.TransitRouterFactory;
import org.matsim.pt.router.TransitRouterImplFactory;

/**
 * MATSim router. The class reuses the functionality available already in
 * MATSim.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class MATSimRouter {

	public static int DEFAULT_LINK_PARKING_CAPACITY = 2;

	private final Controler controler;
	private final TripRouterFactory tripRouterFactory;

	public MATSimRouter(Controler controler, TravelTime travelTime) {
		this.tripRouterFactory = new WithinDayTripRouterFactory(controler,
				travelTime);
		this.controler = controler;
	}

	/*
	 * XXX possibly remove

	public String routeRandomly(Id currentLinkId) {
		Link currentLink = controler.getScenario().getNetwork().getLinks()
				.get(currentLinkId);
		if (currentLink == null)
			return null;
		Object[] outLinks = currentLink.getToNode().getOutLinks().keySet()
				.toArray();
		int idx = MatsimRandom.getRandom().nextInt(outLinks.length);
		return outLinks[idx].toString();
	}
	*/

	public Link findLinkById(Id id) {
		return controler.getNetwork().getLinks().get(id);
	}

	public int getLinkParkingCapacity(Id linkId) {
		// TODO: Needs to be changed
		return DEFAULT_LINK_PARKING_CAPACITY;
	}

	/**
	 * @param linkFrom
	 *            start link
	 * @param linkTo
	 *            end link
	 * @param departureTime
	 *            departure time
	 * @param person
	 *            person for who the route is calculated
	 * @return
	 */
	public List<Id> route(Link linkFrom, Link linkTo) {
		TripRouter tr = tripRouterFactory.createTripRouter();
		List<? extends PlanElement> legs = tr.calcRoute("car", new LinkWrapper(
				linkFrom), new LinkWrapper(linkTo), 0.0, null);
		if (legs.size() == 0) {
			throw new RuntimeException("Route calculation failed: "
					+ linkFrom.getId().toString() + " - "
					+ linkTo.getId().toString());
		}
		Leg leg = (Leg) legs.get(0);
		List<Id> route = new LinkedList<>(((NetworkRoute) leg.getRoute()).getLinkIds());
		if (!route.contains(linkTo.getId()) && !linkTo.equals(linkFrom))
			route.add(linkTo.getId());
		return route;
	}

	public List<Id> route(Id from, Id to) {
		Link fromLink = controler.getNetwork().getLinks().get(from);
		Link toLink = controler.getNetwork().getLinks().get(to);
		return route(fromLink, toLink);
	}

	public Map<Id, ? extends Link> getLinks() {
		return controler.getNetwork().getLinks();
	}
	
	public Link getLink(Id linkId) {
		return controler.getNetwork().getLinks().get(linkId);
	}

	public List<Id> getAdjacentLinks(Id linkId) {
		Link link = controler.getNetwork().getLinks().get(linkId);
		if (link == null)
			return null;
		Set<Id> outLinks = link.getToNode().getOutLinks().keySet();
		Set<Id> inLinks = link.getFromNode().getInLinks().keySet();
		List<Id> links = new LinkedList<Id>();
		links.addAll(inLinks);
		links.addAll(outLinks);
		return links;
	}

	private static class LinkWrapper implements Facility {

		private final Link link;

		public LinkWrapper(Link link) {
			this.link = link;
		}

		public Coord getCoord() {
			return link.getCoord();
		}

		public Id getId() {
			throw new UnsupportedOperationException();
		}

		public Map<String, Object> getCustomAttributes() {
			return null;
		}

		public Id getLinkId() {
			return link.getId();
		}

	}

	private static class WithinDayTripRouterFactory implements
			TripRouterFactory {

		private final Controler controler;
		private final TravelTime travelTime;

		public WithinDayTripRouterFactory(Controler controler,
				TravelTime travelTime) {
			this.controler = controler;
			this.travelTime = travelTime;
		}

		public TripRouter createTripRouter() {
			Config config = controler.getConfig();
			TransitRouterFactory trf = new TransitRouterImplFactory(controler
					.getScenario().getTransitSchedule(),
					new TransitRouterConfig(config.planCalcScore(), config
							.plansCalcRoute(), config.transitRouter(), config
							.vspExperimental()));
			return new TripRouterFactoryImpl(controler.getScenario(),
					controler.getTravelDisutilityFactory(), travelTime,
					new DijkstraFactory(), trf).createTripRouter();
		}

	}
}
