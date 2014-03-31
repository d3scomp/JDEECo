package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class AreaNetworkRegistry {

	private static AreaNetworkRegistry INSTANCE;

	private final Map<Area, List<PositionAwareComponent>> componentsByArea = new HashMap<Area, List<PositionAwareComponent>>();

	private AreaNetworkRegistry() {
	}
	
	public Collection<Area> getAreas() {
		return componentsByArea.keySet();
	}

	public synchronized static AreaNetworkRegistry getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AreaNetworkRegistry();
		}
		return INSTANCE;
	}

	public void initialize(Collection<Area> areas) {
		if (componentsByArea.isEmpty()) {
			for (Area area : areas) {
				componentsByArea.put(area, new LinkedList<PositionAwareComponent>());
			}
		}
	}

	public void addComponent(PositionAwareComponent component) {
		for (Area area : componentsByArea.keySet()) {
			if (area.isInArea(component.position)) {
				componentsByArea.get(area).add(component);
			}
		}
	}
	
	public List<Area> getTeamSites(String teamId) {
		List<Area> result = new LinkedList<>();
		for (Area area : componentsByArea.keySet())
			if (Arrays.binarySearch(area.getTeams(), teamId) > -1)
				result.add(area);
		return result;
	}
	
	public Collection<PositionAwareComponent> getComponentsOfArea(Area area) {
		return componentsByArea.get(area);
	}
	
	public List<PositionAwareComponent> getMembersBelongingToTeam(String teamId, Area area) {
		List<PositionAwareComponent> result = new LinkedList<>();
		if (Arrays.binarySearch(area.getTeams(), teamId) < 0)
			return result;
		Member m;
		for (PositionAwareComponent c : componentsByArea.get(area)) {
			if (c instanceof Member) {
				m = (Member) c;
				if (m.teamId.equals(teamId))
					result.add(c);
			}
		}
		return result;
	}
	
	public List<PositionAwareComponent> getComponentsInArea(Area area) {
		return new LinkedList<>(componentsByArea.get(area));		
	}

}
