package cz.cuni.mff.d3s.jdeeco.matsim.simulation;

import java.util.Collection;
import java.util.Map;

public class DefaultMATSimUpdater implements MATSimUpdater {

	@SuppressWarnings("unchecked")
	@Override
	public void updateJDEECoAgents(Object input, Collection<JDEECoAgent> agents) {
		if (input != null && input instanceof Map<?, ?>) {
			Map<String, MATSimInput> map = (Map<String, MATSimInput>) input;
			for (JDEECoAgent agent: agents) {
					if (map.containsKey(agent.getId())) {
						agent.setRoute(map.get(agent.getId()).route);
						agent.setSpeed(map.get(agent.getId()).speed);
					}
			}
		}
	}

}
