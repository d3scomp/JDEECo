package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class AreaNetworkRegistry {

	private static AreaNetworkRegistry INSTANCE;

	private final Map<Area, List<String>> jDEECoMoudlesByArea = new HashMap<Area, List<String>>();

	private AreaNetworkRegistry() {
	}

	public synchronized static AreaNetworkRegistry getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AreaNetworkRegistry();
		}
		return INSTANCE;
	}

	public void initialize(Collection<Area> areas) {
		if (jDEECoMoudlesByArea.isEmpty()) {
			for (Area area : areas) {
				jDEECoMoudlesByArea.put(area, new LinkedList<String>());
			}
		}
	}

	public void addjDEECoModule(String jDEECoModuleId, Position position) {
		for (Area area : jDEECoMoudlesByArea.keySet()) {
			if (area.isInArea(position)) {
				jDEECoMoudlesByArea.get(area).add(jDEECoModuleId);
			}
		}
	}

	public String getRandomRecipient(String host) {
		List<Area> toPickFrom = new LinkedList<>(jDEECoMoudlesByArea.keySet());
		List<Area> toOmmit = new LinkedList<>();
		for (Area area : toPickFrom) {
			if (jDEECoMoudlesByArea.get(area).contains(host)) {
				toOmmit.add(area);
			}
		}
		toPickFrom.removeAll(toOmmit);
		if (toPickFrom.isEmpty()) {
			return null;
		} else {
			Area area = toPickFrom.get(new Random().nextInt(toPickFrom.size()));
			return jDEECoMoudlesByArea.get(area).get(new Random().nextInt(jDEECoMoudlesByArea.get(area).size()));
		}
	}

	public Collection<String> getRandomRecipients(String host) {
		List<Area> toPickFrom = new LinkedList<>(jDEECoMoudlesByArea.keySet());
		List<Area> toOmmit = new LinkedList<>();
		for (Area area : toPickFrom) {
			if (jDEECoMoudlesByArea.get(area).contains(host)) {
				toOmmit.add(area);
			}
		}
		toPickFrom.removeAll(toOmmit);
		if (toPickFrom.isEmpty()) {
			return null;
		} else {
			Area area = toPickFrom.get(new Random().nextInt(toPickFrom.size()));
			return jDEECoMoudlesByArea.get(area);
		}
	}

}
