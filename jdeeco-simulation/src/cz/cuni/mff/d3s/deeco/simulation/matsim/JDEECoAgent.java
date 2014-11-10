package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.LinkedList;
import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.api.experimental.events.AgentArrivalEvent;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;

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
	private Id plannedVehicleId;

	private QSim simulation;
	private List<Id> route;

	// We need this variable to prevent the stopping the car (i.e. setting the
	// route to empty or null) when it is at the intersection (i.e. in the link
	// buffer).
	private boolean unreachedDestinationGiven = false;

	public JDEECoAgent(Id id, Id currentLinkId, List<Id> route) {
		this.id = id;
		this.currentLinkId = currentLinkId;
		this.route = route;
	}

	public JDEECoAgent(Id id, Id currentLinkId) {
		this(id, currentLinkId, null);
	}

	public void setSimulation(QSim simulation) {
		this.simulation = simulation;
	}

	public void abort(double now) {
		this.state = State.ABORT;
	}

	public void endActivityAndComputeNextState(double now) {
		if (route == null || route.isEmpty())
			return;
		this.simulation.getEventsManager().processEvent(
				this.simulation
						.getEventsManager()
						.getFactory()
						.createActivityEndEvent(now, this.getId(),
								currentLinkId, new IdImpl(100), "activity"));
		this.state = State.LEG; // want to move
	}

	public void endLegAndComputeNextState(double now) {
		this.simulation.getEventsManager().processEvent(
				new AgentArrivalEvent(now, this.getId(), destination(),
						getMode()));
		this.state = State.ACTIVITY;
	}

	public double getActivityEndTime() {
		if (route == null || route.isEmpty()) {
			double currentTime = this.simulation.getSimTimer().getTimeOfDay();
			return currentTime
					+ this.simulation.getSimTimer().getSimTimestepSize();
		} else {
			return simulation.getSimTimer().getTimeOfDay();
		}
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

	public void notifyArrivalOnLinkByNonNetworkMode(Id linkId) {
		this.currentLinkId = linkId;
	}

	public void setRoute(List<Id> route) {
		int indexOfCurrentLink;
		if (unreachedDestinationGiven && (route == null || route.isEmpty())) {
			if (this.route != null && !this.route.isEmpty()) {
				indexOfCurrentLink = this.route.lastIndexOf(currentLinkId);
				if (indexOfCurrentLink < 0) {
					this.route = new LinkedList<>(this.route.subList(0, 1));
				} else if (indexOfCurrentLink < this.route.size() - 1){
					this.route = new LinkedList<>(this.route.subList(indexOfCurrentLink + 1, indexOfCurrentLink + 2));
				} else {
					this.route = new LinkedList<>();
				}
				//System.out.println(id + " route: " + this.route.toString());
				return;
			}
		}
		indexOfCurrentLink = route.lastIndexOf(currentLinkId);
		if (indexOfCurrentLink < 0) {
			this.route = route;
		} else {
			this.route = new LinkedList<Id>(route.subList(indexOfCurrentLink + 1,
					route.size()));
		}
		//System.out.println(id + " route: " + this.route.toString());
	}

	public Id getCurrentLinkId() {
		//System.out.println(id + " current: " + currentLinkId.toString());
		return this.currentLinkId;
	}

	public Id getDestinationLinkId() {
		Id result = destination();
		// Here we mark the situation when the agent was asked for the
		// destination and it replied with one which is not yet reached. From
		// now on and until invoking the chooseNextLink method setting an empty
		// route is not allowed.
		unreachedDestinationGiven = !result.equals(currentLinkId);
		//System.out.println(id + " destination: " + result);
		return result;
	}

	public Id getId() {
		return this.id;
	}

	public Id chooseNextLinkId() {
		this.unreachedDestinationGiven = false;
		Id result;
		if (route == null || route.isEmpty()) {
			result = null;
		} else if (currentLinkId.equals(route.get(route.size()-1))) {
			route.clear();
			result = null;
		} else {
			result = route.remove(0);
			if (result.equals(currentLinkId)) {
				if (route.isEmpty()) {
					result = null;
				} else {
					result = route.remove(0);
				}
			}
		}
		//System.out.println(id + " nextLink: " + result);
		return result;
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

	public void notifyMoveOverNode(Id newLinkId) {
		this.currentLinkId = newLinkId;
	}

	public void setVehicle(MobsimVehicle veh) {
		this.vehicle = veh;
	}

	private Id destination() {
		Id result;
		if (route == null || route.isEmpty()) {
			result = currentLinkId;
		} else {
			result = route.get(route.size() - 1);
		}
		return result;
	}
}