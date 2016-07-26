package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;

public interface MATSimUpdater {
	public void updateJDEECoAgents(Object input, Collection<JDEECoAgent> agents);
}
