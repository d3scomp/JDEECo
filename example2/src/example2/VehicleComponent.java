package example2;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;

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
import cz.cuni.mff.d3s.deeco.simulation.matsim.MATSimRouter;
import cz.cuni.mff.d3s.deeco.simulation.matsim.MATSimTimeProvider;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
public class VehicleComponent {

	public static enum State { STARTING, DRIVING, WAITING, FOLLOWING };
	
	/**
	 * The time (in seconds) to elapse before a vehicle chooses and pursues another destination. 
	 */
	public static final double TIME_BETWEEN_DRIVING = 120.0;
	
	/**
	 * Id of the vehicle component.
	 */
	public String id;

	/**
	 * State of the component - STARTING, DRIVING, WAITING, FOLLOWING
	 */
	public State state;

	/**
	 * This is where the vehicle is heading to.
	 */
	public Id destinationLink;

	/**
	 * Position of the current link.
	 */
	public Coord position;
	
	/**
	 * When to again set of for the next destination.
	 */
	public double startTimeSecs;

	/**
	 * Contains a list of link ids that lead to the destination. It is given to
	 * the MATSim to guide the vehicle which way it should go.
	 */
	public List<Id> route;
	

	/**
	 * This is the destination of the leader. If this component is in FOLLOWING state, it follows a route to this destination instead of doing anything else.
	 */
	public Id leaderDestinationLink;
	
	
	@Local public Actuator<List<Id>> routeActuator;
	
	@Local public Sensor<Id> currentLinkSensor;
	@Local public Sensor<Boolean> isParkedSensor;
	@Local public Sensor<Integer> currentLinkFreeCapacitySensor;
	
	@Local public MATSimRouter router;
	@Local public MATSimTimeProvider clock;

	@Local public Random random;

	public VehicleComponent(String id, ActuatorProvider actuatorProvider, SensorProvider sensorProvider, MATSimRouter router, MATSimTimeProvider clock) {
		this.id = id;

		this.state = State.WAITING;
		this.startTimeSecs = 0;

		this.routeActuator = actuatorProvider.createActuator(ActuatorType.ROUTE);

		this.currentLinkSensor = sensorProvider.createSensor(SensorType.CURRENT_LINK);
		this.isParkedSensor = sensorProvider.createSensor(SensorType.IS_PARKED);
		this.currentLinkFreeCapacitySensor = sensorProvider.createSensor(SensorType.CURRENT_LINK_FREE_PLACES);
		
		this.router = router;
		this.clock = clock;
		this.random = new Random(id.hashCode());
	}

	/**
	 * Periodically prints out the values of sensors and important values of the knowledge.
	 */
	@Process
	@PeriodicScheduling(period=1000, order=10)
	public static void reportStatus(
			@In("id") String id,
			@In("state") State state, 
			@In("currentLinkSensor") Sensor<Id> currentLinkSensor,
			@In("isParkedSensor") Sensor<Boolean> isParkedSensor,
			@In("destinationLink") Id destinationLink,
			@In("route") List<Id> route,
			@In("clock") MATSimTimeProvider clock
			) {
		
		System.out.format("<%s> [%s]  state: %s  currentLink: %s  isParked: %s  destinationLink: %s  route: %s\n", getTime(clock), id, state, currentLinkSensor.read(), isParkedSensor.read(), destinationLink, route);
	}

	/**
	 * Periodically updates knowledge based on sensor readings. These knowledge fields are updated: currentLink.
	 */
	@Process
	@PeriodicScheduling(period=1000, order=0)
	public static void planAndDrive(
			@In("id") String id,
			@InOut("state") ParamHolder<State> state,
			@InOut("startTimeSecs") ParamHolder<Double> startTimeSecs,
			@InOut("destinationLink") ParamHolder<Id> destinationLink,
			@Out("position") ParamHolder<Coord> position,
			@InOut("route") ParamHolder<List<Id>> route,
			@In("leaderDestinationLink") Id leaderDestinationLink,
			@In("currentLinkSensor") Sensor<Id> currentLinkSensor,
			@In("isParkedSensor") Sensor<Boolean> isParkedSensor,
			@In("routeActuator") Actuator<List<Id>> routeActuator,
			@In("clock") MATSimTimeProvider clock,
			@In("router") MATSimRouter router,
			@In("random") Random random) {
		
		Id currentLink = currentLinkSensor.read();
		boolean isParked = isParkedSensor.read();
		
		if (leaderDestinationLink != null) {
			state.value = State.FOLLOWING;
			destinationLink.value = leaderDestinationLink;
			
		} else if (state.value == State.WAITING && clock.getMATSimSeconds() > startTimeSecs.value) {
			
			// Select random destination
			Set<Id> linkIds = router.getLinks().keySet();			
			do {
				int nth = random.nextInt(linkIds.size());
				for (Iterator<Id> iter = linkIds.iterator(); nth>=0; nth--) {
					destinationLink.value = iter.next();
				}
			} while (destinationLink.value.equals(currentLink));
		
			
			System.out.format("<%s> [%s]  setting off to new destination %s\n", getTime(clock), id, destinationLink.value);
			state.value = State.STARTING;
			
		} else if (state.value == State.STARTING && !isParked) {
			state.value = State.DRIVING;
			
		} else if (state.value == State.DRIVING && isParked) {			
			state.value = State.WAITING;
			startTimeSecs.value = clock.getMATSimSeconds() + TIME_BETWEEN_DRIVING;

			System.out.format("<%s> [%s]  vehicle reached its destination\n", getTime(clock), id);
		}
		
		if (state.value == State.STARTING || state.value == State.DRIVING || state.value == State.FOLLOWING) {
			route.value = router.route(currentLink, destinationLink.value);
			routeActuator.set(route.value);			
		}
		
		position.value = router.getLink(currentLink).getCoord();
	}
	
	private static String getTime(MATSimTimeProvider clock) {
		long ts = clock.getMATSimMilliseconds();
		int msec = (int)(ts % 1000); ts = ts / 1000;
		int sec = (int)(ts % 60); ts = ts / 60;  
		int min = (int)(ts % 60); ts = ts / 60;
		int hour = (int)ts;
		
		return String.format("%02d:%02d:%02d.%03d", hour, min, sec, msec);
	}
}
