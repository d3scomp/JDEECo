package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public enum NetworkRegistry {
	INSTANCE;
	
	private final Map<Area, Set<String>> jDEECoMoudlesByArea = new HashMap<Area, Set<String>>();
	
	public void init(Collection<Area> areas) {
		for (Area area : areas) {
			jDEECoMoudlesByArea.put(area, new HashSet<String>());
		}
	}
	
	public void addjDEECoModule(String jDEECoModuleId, Position position) {
		for (Area area : jDEECoMoudlesByArea.keySet()) {
			if (area.isInArea(position)) {
				jDEECoMoudlesByArea.get(area).add(jDEECoModuleId);
				break;
			}
		}
	}
	
	public Set<String> getDifferentAreaMembers(String jDEECoModuleId) {
		List<Area> toPickFrom = new LinkedList<>(jDEECoMoudlesByArea.keySet());
		List<Area> toOmmit = new LinkedList<>();
		for (Area area : toPickFrom) {
			if (jDEECoMoudlesByArea.get(area).contains(jDEECoModuleId)) {
				toOmmit.add(area);
			}
		}
		toPickFrom.removeAll(toOmmit);
		if (toPickFrom.isEmpty()) {
			return new HashSet<>();
		} else {
			return jDEECoMoudlesByArea.get(toPickFrom.get(new Random().nextInt(toPickFrom.size())));
		}
	}
	
	
}
