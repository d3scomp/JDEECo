package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.QSimUtils;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;

import cz.cuni.mff.d3s.deeco.logging.Log;

public class jDEECoAgentSource implements AdditionAwareAgentSource, jDEECoAgentProvider {
	private QSim qSim;
	private final List<jDEECoAgent> agents;

	
	public jDEECoAgentSource() {
		this.agents = new LinkedList<jDEECoAgent>();
	}
	
	public void agentSourceAdded(QSim qSim) {
		this.qSim = qSim;
	}
	
	public void addAgent(jDEECoAgent agent) {
		agents.add(agent);
	}

	public void insertAgentsIntoMobsim() {
		if (qSim == null) {
			Log.e("jDEECoAgentSource not properly initialized!");
			return;
		}
		for (jDEECoAgent agent : agents) {
			MobsimVehicle vehicle = QSimUtils.createDefaultVehicle(new IdImpl(
					agent.getId().toString() + "-vehicle"));
			agent.setSimulation(qSim);
			agent.setPlannedVehicleId(vehicle.getId());
			agent.setVehicle(vehicle);
			qSim.addParkedVehicle(vehicle, agent.getCurrentLinkId());
			qSim.insertAgentIntoMobsim(agent);
		}
	}

	public Collection<jDEECoAgent> getAgents() {
		return agents;
	}
}
