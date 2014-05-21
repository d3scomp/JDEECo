package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.api.experimental.events.AgentArrivalEvent;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;
import org.matsim.core.mobsim.qsim.interfaces.Netsim;

import cz.cuni.mff.d3s.deeco.logging.Log;

public class jDEECoAgent implements MobsimDriverAgent {

	private MobsimVehicle vehicle;
	private Id currentLinkId;
	private Id id;
	private State state = State.ACTIVITY;
	private Id destinationLinkId;
	private Id plannedVehicleId;
	private List<Id> route;
	private Id nextLinkId;
	
	private Netsim simulation;

	private double activityEndTime = 0.; //in seconds

	public jDEECoAgent(Id id, Id currentLinkId, List<Id> route, Id destinationLinkId) {
		this.id = id;
		this.destinationLinkId = destinationLinkId;
		this.currentLinkId = currentLinkId;
		this.route = route;
		/**
		 * Initialize next link id
		 */
		if (route != null) {
			if (route.size() == 0) {
				nextLinkId = destinationLinkId;
			} else if (route.get(0).equals(currentLinkId)) {
				if (route.size() == 1) {
					nextLinkId = destinationLinkId;
				} else {
					nextLinkId = route.get(1);
				}
			} else {
				nextLinkId = route.get(0);
			}	
		}
	}
	
	public void setSimulation(Netsim simulation) {
		this.simulation = simulation;
	}

	public void abort(double now) {
		this.state = State.ABORT;
	}

	public void endActivityAndComputeNextState(double now) {
		this.simulation.getEventsManager().processEvent(
				this.simulation.getEventsManager().getFactory().createActivityEndEvent(
						now, this.getId(), currentLinkId, new IdImpl(100), "parking"));
		this.state = State.LEG; // want to move
	}

	public void endLegAndComputeNextState(double now) {
		this.simulation.getEventsManager().processEvent(new AgentArrivalEvent(
				now, this.getId(), this.getDestinationLinkId(), getMode()));
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
		Log.e("jDEECoAgent "+id.toString()+" destination read");
		return this.destinationLinkId;
	}
	
	public void setDestinationLinkId(Id destinationLinkId) {
		this.destinationLinkId = destinationLinkId;
	}

	public Id getId() {
		return this.id;
	}

	public Id chooseNextLinkId() {
		Log.e("jDEECoAgent "+id.toString()+" next read");
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

	public void notifyMoveOverNode(Id newLinkId) {
		this.currentLinkId = newLinkId;
		updateNextLink();
	}

	public void setVehicle(MobsimVehicle veh) {
		this.vehicle = veh;
	}
	
	private void updateNextLink() {
		if (route != null && !route.isEmpty()) {
			int index = route.indexOf(currentLinkId);
			if (index < 0) {
				this.nextLinkId = route.get(0);
			} else if (index != route.size() - 1) {
				this.nextLinkId = route.get(index + 1);
			}
		}
	}

}