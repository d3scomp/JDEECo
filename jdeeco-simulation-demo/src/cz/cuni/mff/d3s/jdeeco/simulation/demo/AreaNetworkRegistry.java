package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public enum AreaNetworkRegistry {
	INSTANCE;
	

	private final Map<Area, List<PositionAwareComponent>> componentsByArea = new HashMap<Area, List<PositionAwareComponent>>();
	private final Set<PositionAwareComponent> componentsOutside = new HashSet<>();
	private final Map<String, Set<Area>> teamAreas = new HashMap<String, Set<Area>>(); 
	private AreaNetworkRegistry() {
	}
	
	public Collection<Area> getAreas() {
		return componentsByArea.keySet();
	}

	public synchronized static AreaNetworkRegistry getInstance() {
		return INSTANCE;
	}

	public void initialize(Collection<Area> areas) {
		if (componentsByArea.isEmpty()) {
			for (Area area : areas) {
				componentsByArea.put(area, new LinkedList<PositionAwareComponent>());
				for (String team: area.getTeams()) {
					if (!teamAreas.containsKey(team)) {
						teamAreas.put(team, new HashSet<Area>());
					}
					teamAreas.get(team).add(area);
				}
			}
		}
	}

	public void addComponent(PositionAwareComponent component) {
		boolean isInAnArea = false;
		for (Area area : componentsByArea.keySet()) {
			if (area.isInArea(component.position)) {
				componentsByArea.get(area).add(component);
				isInAnArea = true;
			}
		}
		// components not in any area are considered outside
		if (!isInAnArea) {
			componentsOutside.add(component);
		}
	}
	
	public List<Area> getTeamSites(String teamId) {
		return new ArrayList<>(teamAreas.get(teamId));		
	}		
	
	public List<PositionAwareComponent> getComponentsInArea(Area area) {
		return new LinkedList<>(componentsByArea.get(area));		
	}
	
	public Set<PositionAwareComponent> getComponentsOutside() {
		return new HashSet<>(componentsOutside);		
	}

}
