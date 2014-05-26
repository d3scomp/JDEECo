package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.List;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.api.experimental.events.AgentArrivalEvent;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;
import org.matsim.core.mobsim.qsim.interfaces.Netsim;
import org.matsim.core.mobsim.qsim.qnetsimengine.QVehicle;
import org.matsim.core.utils.geometry.CoordImpl;

/**
 * JDEECo agent implementation. The agent is used by the
 * {@link JDEECoWithinDayMobsimListener} to steer the MATSim simulation,
 * according to the state of the JDEECo component.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class JDEECoAgent implements MobsimDriverAgent {

	private MobsimVehicle vehicle;
	private Id currentLinkId;
	private Id id;
	private State state = State.ACTIVITY;
	private Id destinationLinkId;
	private Id plannedVehicleId;
	private List<Id> route;
	private Id nextLinkId;
	private String activityType;

	private Netsim simulation;

	private double activityEndTime; // in seconds

	public JDEECoAgent(Id id, Id currentLinkId, List<Id> route,
			Id destinationLinkId, String activityType, double activityEndTime) {
		this.id = id;
		this.destinationLinkId = destinationLinkId;
		this.activityEndTime = activityEndTime;
		this.currentLinkId = currentLinkId;
		this.route = route;
		this.activityType = activityType;
		/**
		 * Initialize next link id
		 */
		updateNextLink();
	}

	public void setSimulation(Netsim simulation) {
		this.simulation = simulation;
	}

	public void abort(double now) {
		this.state = State.ABORT;
	}

	public void endActivityAndComputeNextState(double now) {
		this.simulation.getEventsManager().processEvent(
				this.simulation
						.getEventsManager()
						.getFactory()
						.createActivityEndEvent(now, this.getId(),
								currentLinkId, new IdImpl(100), activityType));
		this.state = State.LEG; // want to move
	}

	public void endLegAndComputeNextState(double now) {
		this.simulation.getEventsManager().processEvent(
				new AgentArrivalEvent(now, this.getId(), this
						.getDestinationLinkId(), getMode()));
		this.state = State.ACTIVITY;
	}

	public double getActivityEndTime() {
		return this.activityEndTime;
	}

	public void setActivityEndTime(double value) {
		this.activityEndTime = value;
	}

	public Double getExpectedTravelTime() {
		return 0.; // what does this matter for?
	}

	public String getMode() {
		return TransportMode.car; // either car or nothing
	}

	public State getState() {
		return this.state;
	}

	public void setActivityType(String value) {
		this.activityType = value;
	}

	public void notifyArrivalOnLinkByNonNetworkMode(Id linkId) {
		this.currentLinkId = linkId;
		updateNextLink();
	}

	public void setRoute(List<Id> route) {
		this.route = route;
		updateNextLink();
	}

	public Id getCurrentLinkId() {
		return this.currentLinkId;
	}

	public Id getDestinationLinkId() {
		return this.destinationLinkId;
	}

	public void setDestinationLinkId(Id destinationLinkId) {
		this.destinationLinkId = destinationLinkId;
	}

	public Id getId() {
		return this.id;
	}

	public Id chooseNextLinkId() {
		return nextLinkId;
	}

	public Id getPlannedVehicleId() {
		return this.plannedVehicleId;
	}

	public void setPlannedVehicleId(Id plannedVehicleId) {
		this.plannedVehicleId = plannedVehicleId;
	}

	public MobsimVehicle getVehicle() {
		return this.vehicle;
	}

	public Coord estimatePosition(double now) {
		if (vehicle != null) {
			QVehicle qVehicle = (QVehicle) vehicle;
			Link link = qVehicle.getCurrentLink();
			double time = qVehicle.getEarliestLinkExitTime() - now;
			if (time <= 0) {
				return qVehicle.getCurrentLink().getToNode().getCoord();
			}
			double velocity = (link.getFreespeed() > qVehicle
					.getMaximumVelocity()) ? qVehicle.getMaximumVelocity()
					: link.getFreespeed();
			double remainingDistance = velocity * time;
			double distanceDriven = link.getLength() - remainingDistance;
			Coord from = link.getFromNode().getCoord();
			Coord to = link.getToNode().getCoord();
			double estX = ((distanceDriven * (to.getX() - from.getX())) / link
					.getLength()) + from.getX();
			double estY = ((distanceDriven * (to.getY() - from.getY())) / link
					.getLength()) + from.getY();
			return new CoordImpl(estX, estY);
		}
		return null;
	}

	public void notifyMoveOverNode(Id newLinkId) {
		this.currentLinkId = newLinkId;
		updateNextLink();
	}

	public void setVehicle(MobsimVehicle veh) {
		this.vehicle = veh;
	}

	private void updateNextLink() {
		if (currentLinkId.equals(destinationLinkId)) {
			this.nextLinkId = null;
			return;
		}
		if (route != null && !route.isEmpty()) {
			int index = route.indexOf(currentLinkId);
			if (index < 0) {
				this.nextLinkId = route.get(0);
			} else if (index == route.size() - 1) {
				this.nextLinkId = destinationLinkId;
			} else if (index < route.size() - 1) {
				this.nextLinkId = route.get(index + 1);
			}
		} else {
			this.nextLinkId = null;
		}
	}

}