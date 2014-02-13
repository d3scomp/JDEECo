package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum TeamLocationService {
	INSTANCE;
	
	private final Map<String, Set<Area>> teamSites = new HashMap<>();
	
	
	public void init(Set<Area> sites) {	
		for(Area a: sites) {
			for (String team: a.getTeams()) {
				if (!teamSites.containsKey(team))
					teamSites.put(team, new HashSet<Area>());
				teamSites.get(team).add(a);
			}
		}
	}
	
	public boolean isAtTheTeamsSite(String teamId, Position pos) {
		try {
			for (Area site: teamSites.get(teamId)) {		
				if (site.isInArea(pos))
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
