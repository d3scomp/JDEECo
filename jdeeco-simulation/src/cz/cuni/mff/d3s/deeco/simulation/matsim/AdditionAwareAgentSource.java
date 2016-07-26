package cz.cuni.mff.d3s.deeco.simulation.matsim;

import org.matsim.core.mobsim.framework.AgentSource;
import org.matsim.core.mobsim.qsim.QSim;

/**
 * This interface has been introduced to allow creation/modification of the
 * MATSim agent source before the simulation is started. When simulation is
 * started then the QSim instance is created and as such it needs to be passed
 * to the agent source before it is used.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public interface AdditionAwareAgentSource extends AgentSource {
	public void agentSourceAdded(QSim qSim);
}
