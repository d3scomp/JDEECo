package cz.cuni.mff.d3s.jdeeco.matsim.old.simulation;

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
