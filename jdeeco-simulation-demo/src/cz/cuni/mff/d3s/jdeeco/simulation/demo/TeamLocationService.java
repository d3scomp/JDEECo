package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.logging.Log;

public enum TeamLocationService {
	INSTANCE;
	
	private final Map<String, Set<Area>> teamSites = new HashMap<>();
	private final Set<Area> sites = new HashSet<>();
	
	public void init(Set<Area> sites) {	
		this.sites.addAll(sites);
		for(Area a: sites) {
			for (String team: a.getTeams()) {
				if (!teamSites.containsKey(team))
					teamSites.put(team, new HashSet<Area>());
				teamSites.get(team).add(a);
			}
		}
	}
	
	public boolean isAtTheTeamsSite(String teamId, Position pos) {
		// The team has no registered site - it is in the "outside world"
		if (!teamSites.containsKey(teamId)) {
			// then return true if the position is outside every registered area
			for (Area site: sites) {
				if (site.isInArea(pos))
					return false;
			}
			return true;
		}
		// else check whether the position belongs to some of the registered
		// areas for the team 
		for (Area site: teamSites.get(teamId)) {		
			if (site.isInArea(pos))
				return true;
		}
		return false;
	}
	
}
