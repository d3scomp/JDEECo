package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.QSimUtils;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;

import cz.cuni.mff.d3s.deeco.logging.Log;

public class JDEECoAgentSource implements AdditionAwareAgentSource, JDEECoAgentProvider {
	private QSim qSim;
	private final List<JDEECoAgent> agents;

	
	public JDEECoAgentSource() {
		this.agents = new LinkedList<JDEECoAgent>();
	}
	
	public void agentSourceAdded(QSim qSim) {
		this.qSim = qSim;
	}
	
	public void addAgent(JDEECoAgent agent) {
		agents.add(agent);
	}

	public void insertAgentsIntoMobsim() {
		if (qSim == null) {
			Log.e("jDEECoAgentSource not properly initialized!");
			return;
		}
		for (JDEECoAgent agent : agents) {
			MobsimVehicle vehicle = QSimUtils.createDefaultVehicle(new IdImpl(
					agent.getId().toString() + "-vehicle"));
			agent.setSimulation(qSim);
			agent.setPlannedVehicleId(vehicle.getId());
			agent.setVehicle(vehicle);
			qSim.addParkedVehicle(vehicle, agent.getCurrentLinkId());
			qSim.insertAgentIntoMobsim(agent);
		}
	}

	public Collection<JDEECoAgent> getAgents() {
		return agents;
	}
}
