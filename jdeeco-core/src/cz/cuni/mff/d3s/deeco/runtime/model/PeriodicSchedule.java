package cz.cuni.mff.d3s.deeco.runtime.model;

public class PeriodicSchedule extends Schedule {
	
	public static final long DEFAULT_SCHEDULE = 1000;
	
	private long period;
	
	public PeriodicSchedule() {
		this.period = DEFAULT_SCHEDULE;
	}
	
	public PeriodicSchedule(long period) {
		this.period = period;
	}

	public long getPeriod() {
		return period;
	}

}
