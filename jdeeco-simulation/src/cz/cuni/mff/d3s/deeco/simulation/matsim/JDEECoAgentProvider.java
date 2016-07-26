package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;

/**
 * The interface is used by the {@link JDEECoWithinDayMobsimListener} instance
 * to retrieve simulated jDEECo agents.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public interface JDEECoAgentProvider {
	public Collection<JDEECoAgent> getAgents();
}
