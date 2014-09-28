package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.LinkedList;
import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.api.experimental.events.AgentArrivalEvent;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;
import org.matsim.core.mobsim.qsim.interfaces.Netsim;

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

	private Netsim simulation;

	private List<Id> route;

	public JDEECoAgent(Id id, Id currentLinkId, List<Id> route) {
		this.id = id;
		this.currentLinkId = currentLinkId;
		this.route = route;
	}

	public JDEECoAgent(Id id, Id currentLinkId) {
		this(id, currentLinkId, null);
	}

	public void setSimulation(Netsim simulation) {
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
				new AgentArrivalEvent(now, this.getId(), this
						.getDestinationLinkId(), getMode()));
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
		int index = route.indexOf(currentLinkId);
		if (index < 0) {
			this.route = route;
		} else {
			this.route = new LinkedList<Id>(route.subList(index + 1, route.size()));
		}
	}

	public Id getCurrentLinkId() {
		//System.out.println(id.toString() + " getCurrentLinkId " + currentLinkId.toString());
		return this.currentLinkId;
	}

	public Id getDestinationLinkId() {
		if (route == null || route.isEmpty()) {
			return currentLinkId;
		} else {
			return route.get(route.size()-1);
		}
	}

	public Id getId() {
		return this.id;
	}

	public Id chooseNextLinkId() {
		if (route == null || route.isEmpty()) {
			return null;
		} else {
			Id result = route.remove(0);
			if (result.equals(currentLinkId)) {
				if (route.isEmpty()) {
					return null;
				} else {
					return route.remove(0);
				}
			} else {
				return result;
			}
		}
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
}