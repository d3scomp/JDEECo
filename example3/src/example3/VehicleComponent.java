package example3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;

import tutorial.environment.Actuator;
import tutorial.environment.ActuatorProvider;
import tutorial.environment.ActuatorType;
import tutorial.environment.Sensor;
import tutorial.environment.SensorProvider;
import tutorial.environment.SensorType;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.simulation.matsim.MATSimRouter;
import cz.cuni.mff.d3s.deeco.simulation.matsim.MATSimTimeProvider;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
public class VehicleComponent {

	/**
	 * Distance (as crow flies) from the destination within which the vehicle tries to find a parking place and park there. 
	 */
	public static final double PARKING_DISTANCE_THRESHOLD = 4000;

	/**
	 * Constant which when set to the routeActuator, the vehicle stops at the end of the current link.
	 */
	public static final List<Id> EMPTY_ROUTE = new LinkedList<Id>();
	
	/**
	 * Id of the vehicle component.
	 */
	public String id;

	/**
	 * This is where the vehicle is heading to.
	 */
	public Id destinationLink;
	
	/**
	 * Contains a list of link ids that lead to the destination. It is given to
	 * the MATSim to guide the vehicle which way it should go.
	 */
	public List<Id> route;
	
	/**
	 * Link, which has been selected to be parked at. Is <code>null</code> if none has been selected. The link is selected when the vehicle
	 * gets close to its destination. Once it is selected, the vehicle heads to this link instead of to its destination.
	 */
	public Id linkToBeParkedAt;
	
	/**
	 * Link where the vehicle is currently at.
	 */
	public Id currentLink;

	/**
	 * Position of the current link.
	 */
	public Coord position;
	
	/**
	 * Defines how long an entry about a fully occupied link is remembered. After this time, it is forgotten.
	 */
	public static final long OCCUPIED_LINK_ENTRY_VALIDITY=60*1000;
	
	
	public static class LinkCapacityEntry implements Serializable {
		public LinkCapacityEntry(long timestamp, Id linkId, int capacity) {
			this.timestamp = timestamp;
			this.linkId = linkId;
			this.capacity = capacity;
		}
		
		long timestamp; /* in miliseconds */
		Id linkId;
		int capacity;
	}
	/**
	 * A set of links, which have been found fully occupied in the recent time (defined by OCCUPIED_LINK_ENTRY_VALIDITY). The set is kept in a map, which maps Id -> timestamp (in miliseconds since midnights). 
	 */
	public Map<Id, LinkCapacityEntry> linksCapacity = new HashMap<Id, LinkCapacityEntry>();
	

	@Local public Actuator<List<Id>> routeActuator;
	
	@Local public Sensor<Id> currentLinkSensor;
	@Local public Sensor<Boolean> isParkedSensor;
	@Local public Sensor<Integer> currentLinkFreeCapacitySensor;
	
	@Local public MATSimRouter router;
	@Local public MATSimTimeProvider clock;

	public VehicleComponent(String id, Id destinationLink, ActuatorProvider actuatorProvider, SensorProvider sensorProvider, MATSimRouter router, MATSimTimeProvider clock) {
		this.id = id;

		this.destinationLink = destinationLink;

		this.routeActuator = actuatorProvider.createActuator(ActuatorType.ROUTE);

		this.currentLinkSensor = sensorProvider.createSensor(SensorType.CURRENT_LINK);
		this.isParkedSensor = sensorProvider.createSensor(SensorType.IS_PARKED);
		this.currentLinkFreeCapacitySensor = sensorProvider.createSensor(SensorType.CURRENT_LINK_FREE_PLACES);
		
		this.router = router;
		this.clock = clock;
	}

	/**
	 * Periodically prints out the values of sensors and important values of the knowledge.
	 */
	@Process
	@PeriodicScheduling(period=1000, order=10)
	public static void reportStatus(
			@In("id") String id,
			@In("currentLinkSensor") Sensor<Id> currentLinkSensor,
			@In("isParkedSensor") Sensor<Boolean> isParkedSensor,
			@In("currentLinkFreeCapacitySensor") Sensor<Integer> currentLinkFreeCapacitySensor,
			@In("destinationLink") Id destinationLink,
			@In("linkToBeParkedAt") Id linkToBeParkedAt,
			@In("route") List<Id> route,
			@In("clock") MATSimTimeProvider clock
			) {
		
		Log.d("Entry [" + id + "]:reportStatus");

		long ts = clock.getMATSimMilliseconds();
		int msec = (int)(ts % 1000); ts = ts / 1000;
		int sec = (int)(ts % 60); ts = ts / 60;  
		int min = (int)(ts % 60); ts = ts / 60;
		int hour = (int)ts;
		
//		if (id.equals("V1")) {
			System.out.format("<%02d:%02d:%02d.%03d> [%s]  link: %s  currentLinkFreeCapacitySensor: %d  isParked: %s  destinationLink: %s  linkToBeParkedAt: %s  route: %s\n", hour, min, sec, msec, id, currentLinkSensor.read(), currentLinkFreeCapacitySensor.read(), isParkedSensor.read(), destinationLink, linkToBeParkedAt, route);
//		}
//		System.out.format("<%02d:%02d:%02d.%03d> [%s]  link: %s  currentLinkFreeCapacitySensor: %d  isParked: %s  destinationLink: %s  linkToBeParkedAt: %s  route: %s\n", hour, min, sec, msec, id, currentLinkSensor.read(), currentLinkFreeCapacitySensor.read(), isParkedSensor.read(), destinationLink, linkToBeParkedAt, route);
	}

	/**
	 * Periodically updates knowledge based on sensor readings. These knowledge fields are updated: currentLink.
	 */
	@Process
	@PeriodicScheduling(period=1000, order=0)
	public static void updateCurrentLink(@In("id") String id,
			@Out("currentLink") ParamHolder<Id> currentLinkHolder,
			@Out("position") ParamHolder<Coord> position,
			@In("currentLinkSensor") Sensor<Id> currentLinkSensor,
			@In("router") MATSimRouter router) {
		
		Log.d("Entry [" + id + "]:updateCurrentLink");

		currentLinkHolder.value = currentLinkSensor.read();
		position.value = router.getLink(currentLinkHolder.value).getCoord();
	}
	
	/**
	 * Periodically updated the map of fully occupied links by information obtained from the current link free capacity sensor. It removes entries which are older than 
	 */
	@Process
	@PeriodicScheduling(period=1000, order=1)
	public static void updateOccupiedLinks(@In("id") String id,
			@In("currentLink") Id currentLink,
			@In("currentLinkFreeCapacitySensor") Sensor<Integer> currentLinkFreeCapacitySensor,
			@InOut("linksCapacity") ParamHolder<Map<Id, LinkCapacityEntry>> linksCapacity,
			@In("clock") MATSimTimeProvider clock) {
		
		Log.d("Entry [" + id + "]:updateOccupiedLinks");

		linksCapacity.value.put(currentLink, new LinkCapacityEntry(clock.getMATSimMilliseconds(), currentLink, currentLinkFreeCapacitySensor.read()));
		
		Iterator<Map.Entry<Id, LinkCapacityEntry>> entries = linksCapacity.value.entrySet().iterator();
		while (entries.hasNext()) {
		    Map.Entry<Id, LinkCapacityEntry> entry = entries.next();
		    
		    if (entry.getValue().timestamp + OCCUPIED_LINK_ENTRY_VALIDITY < clock.getMATSimMilliseconds()) {
		    	entries.remove();
		    }
		}
	}


	/**
	 * Selects a link, where the vehicle is to park or sets it <code>null</code> if no link can be chosen. The link is selected as soon as a vehicle gets close to its destination.
	 */
	@Process
	@PeriodicScheduling(period=5000, order=2)
	public static void selectLinkToBeParkedAt(@In("id") String id,
			@In("destinationLink") Id destinationLink,
			@In("currentLink") Id currentLink,
			@In("linksCapacity") Map<Id, LinkCapacityEntry> linksCapacity,
			@InOut("linkToBeParkedAt") ParamHolder<Id> linkToBeParkedAt,
			@In("isParkedSensor") Sensor<Boolean> isParkedSensor,
			@In("router") MATSimRouter router) {
	
		Log.d("Entry [" + id + "]:selectLinkToBeParkedAt");

		Coord destinationPosition = router.getLink(destinationLink).getCoord();
		Coord currentLinkPosition = router.getLink(currentLink).getCoord();
		double distanceToDestination = getEuclidDistance(currentLinkPosition, destinationPosition); 

		if (distanceToDestination <= PARKING_DISTANCE_THRESHOLD) {
			if (linkToBeParkedAt.value == null || (!isParkedSensor.read() && isOccupied(linkToBeParkedAt.value, linksCapacity))) {
				linkToBeParkedAt.value = getClosestUnoccupiedLink(router, destinationLink, linksCapacity);
				
				if (linkToBeParkedAt.value == null) {
					// This means there is no unoccupied link in the vicinity. We thus select an arbitrary link close enough and try it out.
					linkToBeParkedAt.value = getRandomLinkCloseBy(router, destinationLink);
				}
			}
		} else {
			linkToBeParkedAt.value = null;
		}
	}		

		
	/** 
	 * Plans the route to the destination or to the linkToBeParkedAt (if this is set) and drives the vehicle accordingly.
	 */
	@Process
	@PeriodicScheduling(period=5000, order=3)
	public static void planRouteAndDrive(@In("id") String id,
			@In("currentLink") Id currentLink,
			@In("destinationLink") Id destinationLink,
			@In("linkToBeParkedAt") Id linkToBeParkedAt,
			@InOut("route") ParamHolder<List<Id>> route,
			@In("routeActuator") Actuator<List<Id>> routeActuator,
			@In("router") MATSimRouter router) {

		Log.d("Entry [" + id + "]:planRouteAndDrive");

		Id linkToNavigateTo = linkToBeParkedAt == null ? destinationLink : linkToBeParkedAt;
		
		route.value = router.route(currentLink, linkToNavigateTo);
		
		routeActuator.set(route.value);
	}

	
	// ------------------------ PRIVATE METHODS ---------------------------------
	
	private static double getEuclidDistance(Coord p1, Coord p2) {
		double dx = p1.getX() - p2.getX();
		double dy = p1.getY() - p2.getY(); 
		
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	private static class LinkInfo implements Comparable<LinkInfo> {
		public LinkInfo(Link link, double distanceSoFar) {
			super();
			this.link = link;
			this.distanceSoFar = distanceSoFar;
		}
		
		Link link;
		double distanceSoFar;
		
		@Override
		public int hashCode() {
			return link.getId().hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			return compareTo((LinkInfo)obj) == 0;
		}

		public int compareTo(LinkInfo o) {
			int result;
			
			result = Double.compare(distanceSoFar, o.distanceSoFar);
			if (result == 0) result = Double.compare(link.getLength(), o.link.getLength());
			if (result == 0) result = link.getId().compareTo(o.link.getId());
			
			return result;
		}
	}

	private static boolean isOccupied(Id linkId, Map<Id, LinkCapacityEntry> linksCapacity) {
		LinkCapacityEntry cap = linksCapacity.get(linkId);
		
		return cap != null && cap.capacity == 0;
	}
	
	/**
	 * Returns links close to the centerLinkId. The links are ordered in the ascending order from the center.
	 */
	private static List<Id> getLinksCloseBy(MATSimRouter router, Id centerLinkId) {
		HashSet<Id> visitedLinks = new HashSet<Id>();
		TreeSet<LinkInfo> toBeVisited = new TreeSet<LinkInfo>();

		toBeVisited.add(new LinkInfo(router.getLink(centerLinkId), 0));

		ArrayList<Id> linksCloseBy = new ArrayList<Id>();
		
		while (!toBeVisited.isEmpty()) {
			LinkInfo li = toBeVisited.pollFirst();
			
			if (!visitedLinks.contains(li.link.getId())) {
				Id linkId = li.link.getId();

				linksCloseBy.add(linkId);

				visitedLinks.add(li.link.getId());
			
				for (Link inLink : li.link.getFromNode().getInLinks().values()) {
					toBeVisited.add(new LinkInfo(inLink, li.distanceSoFar + li.link.getLength()));
				}
			}
		}
		
		return linksCloseBy;
	}
	
	private static Id getRandomLinkCloseBy(MATSimRouter router, Id centerLinkId) {
		List<Id> linksCloseBy = getLinksCloseBy(router, centerLinkId);
		
		return linksCloseBy.get(new Random().nextInt(linksCloseBy.size()));
	}
	
	private static Id getClosestUnoccupiedLink(MATSimRouter router, Id centerLinkId, Map<Id, LinkCapacityEntry> linksCapacity) {
		List<Id> linksCloseBy = getLinksCloseBy(router, centerLinkId);

		for (Id linkId : linksCloseBy) {
			if (!isOccupied(linkId, linksCapacity)) {
				return linkId;
			}
		}
		
		return null;
	}
}
