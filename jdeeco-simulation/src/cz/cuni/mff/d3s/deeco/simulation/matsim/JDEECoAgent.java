package cz.cuni.mff.d3s.deeco.simulation.matsim;

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
	private Id nextLinkId;
	private Id id;
	private State state = State.ACTIVITY;
	private Id plannedVehicleId;

	private Netsim simulation;

	private MATSimInput input;

	public JDEECoAgent(Id id, Id currentLinkId, MATSimInput input) {
		this.id = id;
		this.currentLinkId = currentLinkId;

		this.input = input;
		/**
		 * Initialize next link id
		 */
		if (input != null) {
			updateNextLink();
		}
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
		if (input != null && now == input.activityEndTime) {
			this.simulation.getEventsManager().processEvent(
					this.simulation
							.getEventsManager()
							.getFactory()
							.createActivityEndEvent(now, this.getId(),
									currentLinkId, new IdImpl(100),
									input.activityType));
			this.state = State.LEG; // want to move
			updateNextLink();
		}
	}

	public void endLegAndComputeNextState(double now) {
		this.simulation.getEventsManager().processEvent(
				new AgentArrivalEvent(now, this.getId(), this
						.getDestinationLinkId(), getMode()));
		this.state = State.ACTIVITY;
	}

	public double getActivityEndTime() {
		double currentTime = this.simulation.getSimTimer().getTimeOfDay();
		return currentTime + this.simulation.getSimTimer().getSimTimestepSize();
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

	public void setInput(MATSimInput input) {
		this.input = input;
		updateNextLink();
	}

	public Id getCurrentLinkId() {
		// Log.i(id.toString() + " getCurrentLinkId " +
		// currentLinkId.toString());
		return this.currentLinkId;
	}

	public Id getDestinationLinkId() {
		// Log.i(id.toString() + " getDestination " + input.destination);
		//We assume here input not being null.
		return input.destination;
	}

	public Id getId() {
		return this.id;
	}

	public Id chooseNextLinkId() {
		// Log.i(id.toString() + " chooseNextLinkId " + ((nextLinkId == null) ?
		// "null" :nextLinkId.toString()));
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
		if (state.equals(State.ACTIVITY) && currentLinkId != null) {
			return simulation.getScenario().getNetwork().getLinks()
					.get(currentLinkId).getToNode().getCoord();
		}
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
		if (currentLinkId.equals(input.destination)) {
			this.nextLinkId = null;
			return;
		}
		if (input.route != null) {
			int index = input.route.indexOf(currentLinkId);
			if (index < 0) {
				if (input.route.isEmpty()) {
					this.nextLinkId = input.destination;
				} else {
					this.nextLinkId = input.route.get(0);
				}
			} else if (index == input.route.size() - 1) {
				this.nextLinkId = input.destination;
			} else if (index < input.route.size() - 1) {
				this.nextLinkId = input.route.get(index + 1);
			}
		} else {
			this.nextLinkId = null;
		}
	}

}