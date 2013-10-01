package cz.cuni.mff.d3s.deeco.runtime.model;

import java.util.LinkedList;
import java.util.List;

public class Schedule {
 
	public static final long DEFAULT_PERIOD = 1000;
	public static final long NO_PERIOD = 0;
	
	private long period;
	private List<Trigger> triggers;
	
	public Schedule(long period, List<Trigger> triggers) {
		this.period = period;
		this.triggers = new LinkedList<>();
		if (triggers != null)
			this.triggers.addAll(triggers);
	}
	
	public Schedule() {
		this(DEFAULT_PERIOD, null);
	}

	public long getPeriod() {
		return period;
	}

	public List<Trigger> getTriggers() {
		return triggers;
	}
	
	public boolean isPeriodic() {
		return period != NO_PERIOD;
	}
	
	public boolean isTriggered() {
		return !triggers.isEmpty();
	}
}
