package cz.cuni.mff.d3s.jdeeco.matsim.demo.convoy;

import java.util.List;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.simulation.matsim.MATSimRouter;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.MATSimVehicle;
import cz.cuni.mff.d3s.jdeeco.matsim.old.roadtrains.Actuator;
import cz.cuni.mff.d3s.jdeeco.matsim.old.roadtrains.ActuatorType;
import cz.cuni.mff.d3s.jdeeco.matsim.old.roadtrains.Sensor;
import cz.cuni.mff.d3s.jdeeco.matsim.old.roadtrains.SensorType;

@Component
public class Vehicle {
	/**
	 * Id of the vehicle component.
	 */
	public String id;
	
	/**
	 * Link where the vehicle is currently at.
	 */
	public Id currentLink;
	
	/**
	 * Vehicle speed
	 */
	public Double speed;
	
	/**
	 * Position of the current link.
	 */
	public Coord position;
	
	public Id otherVehicleLink;
	
	/**
	 * Position of the destination
	 */
	public Id dstLinkId;
	
	/**
	 * Contains a list of link ids that lead to the destination. It is given to
	 * the MATSim to guide the vehicle which way it should go.
	 */
	@Local
	public List<Id> route;
	
	@Local
	public Actuator<List<Id> > routeActuator;
	
	@Local
	public Actuator<Double> speedActuator;
	
	@Local
	public Sensor<Id> currentLinkSensor;

	@Local
	public MATSimRouter router;
	
	@Local
	public CurrentTimeProvider clock;
	
	public Long curTime;

	public Vehicle(String id, Coord dst, MATSimVehicle vehiclePlugin) {
		this.id = id;
	
		this.router = vehiclePlugin.getSimulation().getRouter();
		this.routeActuator = vehiclePlugin.getActuatorProvider().createActuator(ActuatorType.ROUTE);
		this.speedActuator = vehiclePlugin.getActuatorProvider().createActuator(ActuatorType.SPEED);
		this.currentLinkSensor = vehiclePlugin.getSensorProvider().createSensor(SensorType.CURRENT_LINK);
		this.clock = vehiclePlugin.getSimulation().getTimer();
		this.dstLinkId = router.findNearestLink(dst).getId();
	}

	/**
	 * Periodically prints out the values of sensors and important values of the
	 * knowledge.
	 * 
	 * This is also responsible for execution of vehicle monitoring calls. The monitoring
	 * calls reproduce dot description of vehicles positions and their relations.
	 */
	@Process
	@PeriodicScheduling(period = 5000, order = 10)
	public static void reportStatus(
			@In("id") String id,
 			@In("currentLink") Id currentLink,
			@In("position") Coord position,
			@In("dstLinkId") Id dstLinkId,
			@In("route") List<Id> route,
			@In("clock") CurrentTimeProvider clock,
			@In("router") MATSimRouter router,
			@In("speed") Double speed,
			@In("otherVehicleLink") Id otherVehicleLink) {
		Log.d("Entry [" + id + "]:reportStatus");
		
		System.out.format("%s %s, pos: %s, dst: %s, speed: %.0f, otherPos: %s%n",
				formatTime(clock.getCurrentMilliseconds()),
				id,
				printPos(currentLink, router),
				dstLinkId.toString(),
				speed,
				printPos(otherVehicleLink, router));
	}
	
	private static String printPos(Id linkId, MATSimRouter router) {
		if(linkId == null) {
			return "UNKNOWN";
		}
		Coord coord = router.findLinkById(linkId).getCoord();
		return String.format("%s (%.0f, %.0f)", linkId.toString(), coord.getX(), coord.getY());
	}

	/**
	 * Periodically updates knowledge based on sensor readings. These knowledge
	 * fields are updated: currentLink, position, curTime.
	 */
	@Process
	@PeriodicScheduling(period = 200, order = 1)
	public static void updateSensors(
			@In("id") String id,
			@Out("currentLink") ParamHolder<Id> currentLinkHolder,
			@Out("position") ParamHolder<Coord> position,
			@In("currentLinkSensor") Sensor<Id> currentLinkSensor,
			@In("router") MATSimRouter router,
			@In("clock") CurrentTimeProvider clock,
			@Out("curTime") ParamHolder<Long> curTime) {
		Log.d("Entry [" + id + "]:updateCurrentLink");
		currentLinkHolder.value = currentLinkSensor.read();
		position.value = router.getLink(currentLinkHolder.value).getCoord();
		curTime.value = clock.getCurrentMilliseconds();
	}
	
	/**
	 * Plans the route to the destination.
	 * 
	 * This can operate with the leader which is followed or without. When the leader is set by road train or
	 * just by leader-follower relation the route to leader is used. When the leader is not available then the
	 * route to destination is used.
	 */
	@Process
	@PeriodicScheduling(period = 2000, order = 4)
	public static void planRouteAndDrive(
			@In("id") String id,
			@In("currentLink") Id currentLink,
			@In("dstLinkId") Id dstLinkId,
			@InOut("route") ParamHolder<List<Id> > route,
			@In("routeActuator") Actuator<List<Id> > routeActuator,
			@In("speedActuator") Actuator<Double> speedActuator,
			@In("router") MATSimRouter router,
			@Out("speed") ParamHolder<Double> speed) throws Exception {
		
		route.value = router.route(currentLink, dstLinkId, route.value);
				
		routeActuator.set(route.value);
	}
	
	private static String formatTime(long ts) {
		int msec = (int) (ts % 1000);
		ts = ts / 1000;
		int sec = (int) (ts % 60);
		ts = ts / 60;
		int min = (int) (ts % 60);
		ts = ts / 60;
		int hour = (int) ts;
		
		return String.format("<%02d:%02d:%02d.%03d>", hour, min, sec, msec);
	}
}
