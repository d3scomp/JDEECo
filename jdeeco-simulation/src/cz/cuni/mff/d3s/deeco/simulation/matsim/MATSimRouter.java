package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.api.experimental.facilities.Facility;
import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.router.TripRouter;
import org.matsim.core.router.TripRouterFactory;
import org.matsim.core.router.TripRouterFactoryImpl;
import org.matsim.core.router.util.DijkstraFactory;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.population.algorithms.XY2Links;
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

	public static int DEFAULT_LINK_PARKING_CAPACITY = 3;

	private final Controler controler;
	private final TripRouterFactory tripRouterFactory;

	public MATSimRouter(Controler controler, TravelTime travelTime) {
		this.tripRouterFactory = new WithinDayTripRouterFactory(controler,
				travelTime);
		this.controler = controler;
		XY2Links xy2Links = new XY2Links(this.controler.getNetwork(),
				((ScenarioImpl) this.controler.getScenario())
						.getActivityFacilities());
		xy2Links.run(controler.getPopulation());
	}

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

	public Link findLinkById(Id id) {
		return controler.getNetwork().getLinks().get(id);
	}

	public int getLinkParkingCapacity(Id linkId) {
		// TODO: Needs to be changed
		return DEFAULT_LINK_PARKING_CAPACITY;
	}

	/**
	 * 
	 * @param from
	 *            start activity id
	 * @param to
	 *            end activity id
	 * @param departureTime
	 *            start activity end time
	 * @param person
	 *            person for who the route is calculated
	 * @return
	 */
	public List<Id> route(Activity from, Activity to, double departureTime,
			Person person) {
		TripRouter tr = tripRouterFactory.createTripRouter();
		List<? extends PlanElement> legs = tr.calcRoute("car",
				new ActivityWrapper(from), new ActivityWrapper(to),
				departureTime, person);
		if (legs.size() == 0) {
			throw new RuntimeException("Person's " + person.getId().toString()
					+ " route calculation failed.");
		}
		Leg leg = (Leg) legs.get(0);
		return ((NetworkRoute) leg.getRoute()).getLinkIds();
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
	public List<Id> route(Link linkFrom, Link linkTo, double departureTime,
			Person person) {
		TripRouter tr = tripRouterFactory.createTripRouter();
		List<? extends PlanElement> legs = tr.calcRoute("car", new LinkWrapper(
				linkFrom), new LinkWrapper(linkTo), departureTime, person);
		if (legs.size() == 0) {
			throw new RuntimeException("Person's " + person.getId().toString()
					+ " route calculation failed.");
		}
		Leg leg = (Leg) legs.get(0);
		return ((NetworkRoute) leg.getRoute()).getLinkIds();
	}

	public List<Id> route(Id from, Id to, double departureTime, Person person) {
		Link fromLink = controler.getNetwork().getLinks().get(from);
		Link toLink = controler.getNetwork().getLinks().get(to);
		return route(fromLink, toLink, departureTime, person);
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

	private static class ActivityWrapper implements Facility {

		private final Activity activity;

		public ActivityWrapper(Activity activity) {
			this.activity = activity;
		}

		public Coord getCoord() {
			return activity.getCoord();
		}

		public Id getId() {
			throw new UnsupportedOperationException();
		}

		public Map<String, Object> getCustomAttributes() {
			return null;
		}

		public Id getLinkId() {
			return activity.getLinkId();
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
