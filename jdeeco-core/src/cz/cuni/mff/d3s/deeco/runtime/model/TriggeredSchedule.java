package cz.cuni.mff.d3s.deeco.runtime.model;

import java.util.LinkedList;
import java.util.List;

public class TriggeredSchedule extends Schedule {
	private List<Trigger> triggers;
	
	public TriggeredSchedule(List<Trigger> triggers) {
		this.triggers = triggers;
	}
	
	public TriggeredSchedule() {
		this(new LinkedList<Trigger>());
	}

	public List<Trigger> getTriggers() {
		return triggers;
	}
}
