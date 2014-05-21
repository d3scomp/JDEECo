package cz.cuni.mff.d3s.deeco.simulation.matsim;

import org.matsim.core.mobsim.framework.AgentSource;
import org.matsim.core.mobsim.qsim.QSim;

public interface AdditionAwareAgentSource extends AgentSource {
	public void agentSourceAdded(QSim qSim);
}
