package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public enum AreaNetworkRegistry {
	INSTANCE;
	

	private Map<Area, List<PositionAwareComponent>> componentsByArea;
	private Set<PositionAwareComponent> componentsOutside;
	private Set<PositionAwareComponent> allComponents;
	private Set<PositionAwareComponent> ipEnabledComponents;	
	private Map<String, Set<Area>> teamAreas; 
	
	private AreaNetworkRegistry() {
		clear();
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
	
	public void clear() {
		componentsByArea = new HashMap<Area, List<PositionAwareComponent>>();
		componentsOutside = new HashSet<>();
		allComponents = new HashSet<>();
		ipEnabledComponents = new HashSet<>();	
		teamAreas = new HashMap<String, Set<Area>>(); 
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
		if (component.hasIP) {
			ipEnabledComponents.add(component);
		}
		allComponents.add(component);
	}
	
	public List<Area> getTeamSites(String teamId) {
		if (teamAreas.containsKey(teamId))
			return new ArrayList<>(teamAreas.get(teamId));
		else
			return Collections.emptyList();
	}		
	
	public Collection<PositionAwareComponent> getComponentsInArea(Area area) {
		return new HashSet<>(componentsByArea.get(area));		
	}
	
	public Collection<PositionAwareComponent> getComponentsOutside() {
		return new HashSet<>(componentsOutside);		
	}
	
	public Collection<PositionAwareComponent> getAllComponents() {
		return allComponents;
	}
	
	public Collection<PositionAwareComponent> getIpEnabledComponents() {
		return ipEnabledComponents;
	}
	
	public boolean isAtTheTeamsSite(String teamId, Position pos) {
		// The team has no registered site - it is in the "outside world"
		if (!teamAreas.containsKey(teamId)) {
			// then return true if the position is outside every registered area
			for (Area site: componentsByArea.keySet()) {
				if (site.isInArea(pos))
					return false;
			}
			return true;
		}
		// else check whether the position belongs to some of the registered
		// areas for the team 
		for (Area site: teamAreas.get(teamId)) {		
			if (site.isInArea(pos))
				return true;
		}
		return false;
	}
}
