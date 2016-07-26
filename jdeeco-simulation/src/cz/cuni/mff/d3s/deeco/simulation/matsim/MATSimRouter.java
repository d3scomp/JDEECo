package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.api.experimental.facilities.Facility;
import org.matsim.core.controler.Controler;
import org.matsim.core.network.NetworkImpl;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.router.TripRouter;
import org.matsim.core.router.TripRouterFactory;
import org.matsim.core.router.TripRouterFactoryImpl;
import org.matsim.core.router.util.DijkstraFactory;
import org.matsim.core.router.util.TravelTime;

/**
 * MATSim router. The class reuses the functionality available already in
 * MATSim.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class MATSimRouter {
	
	private final Controler controler;
	private final TripRouterFactory tripRouterFactory;
	private final int routeCalculationOffset;

	public MATSimRouter(Controler controler, TravelTime travelTime, int routeCalculationOffset) {
		this.tripRouterFactory = new WithinDayTripRouterFactory(controler,
				travelTime);
		this.controler = controler;
		this.routeCalculationOffset = routeCalculationOffset;
	}

	public Link findLinkById(Id id) {
		return controler.getNetwork().getLinks().get(id);
	}
	
	public Link findNearestLink(Coord coord) {
		return ((NetworkImpl)controler.getNetwork()).getNearestLink(coord);
	}
	
	public Link findNearestLinkRight(Coord coord) {
		return ((NetworkImpl)controler.getNetwork()).getNearestRightEntryLink(coord);
	}
	
	public Link findNearestLinkExactly(Coord coord) {
		return ((NetworkImpl)controler.getNetwork()).getNearestLinkExactly(coord);
	}
	
	public Node findNearestNode(Coord coord) {
		return ((NetworkImpl)controler.getNetwork()).getNearestNode(coord);
	}
	
	public Collection<Node> getNearestNodes(Coord coord, double distance) {
		return ((NetworkImpl)controler.getNetwork()).getNearestNodes(coord, distance);
	}
	
	public List<Id> route(Id from, Id to) {
		return route(from, to, null);
	}

	public List<Id> route(Id from, Id to, List<Id> currentRoute) {
		List<Id> routePrefix = null;
		Id newFrom;
		if (currentRoute == null || currentRoute.isEmpty()) {
			newFrom = from;
		} else {
			List<Id> truncatedCurrentRoute = new LinkedList<>(currentRoute);
			int fromIndex = truncatedCurrentRoute.indexOf(from);
			if (fromIndex > -1) {
				truncatedCurrentRoute = truncatedCurrentRoute.subList(fromIndex, truncatedCurrentRoute.size());
			}
			if (routeCalculationOffset < truncatedCurrentRoute.size()) {
				fromIndex = Math.max(0, routeCalculationOffset-1);
			} else {
				fromIndex = truncatedCurrentRoute.size()-1;
			}
			routePrefix = truncatedCurrentRoute.subList(0, fromIndex + 1);
			newFrom = truncatedCurrentRoute.get(fromIndex);
			//System.out.println("from: "+from.toString()+" newFrom: " + newFrom.toString() + " to: " + to.toString());
		}
		Link fromLink = controler.getNetwork().getLinks().get(newFrom);
		Link toLink = controler.getNetwork().getLinks().get(to);
		List<Id> calculatedRoute = route(fromLink, toLink);
		//System.out.println("calculatedRoute: " + calculatedRoute.toString() + " prefix: " + ((routePrefix == null) ? "null" : routePrefix.toString()));
		if (routePrefix != null && !routePrefix.isEmpty()) {
			calculatedRoute.addAll(0, routePrefix);
		}
		return calculatedRoute;
	}
	
	private List<Id> route(Link linkFrom, Link linkTo) {
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
			return new TripRouterFactoryImpl(controler.getScenario(),
					controler.getTravelDisutilityFactory(), travelTime,
					new DijkstraFactory(), null).createTripRouter();
		}

	}
}
