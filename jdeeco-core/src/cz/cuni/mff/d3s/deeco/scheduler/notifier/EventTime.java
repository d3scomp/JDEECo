package cz.cuni.mff.d3s.deeco.scheduler.notifier;

public class EventTime implements Comparable<EventTime> {
	private long timePoint;
	private SchedulerNotifierEventListener listener;
	private boolean terminationEvent;

	public EventTime(long timePoint, SchedulerNotifierEventListener listener, boolean isTerminationEvent) {
		this.setTimePoint(timePoint);
		this.listener = listener;
		this.terminationEvent = isTerminationEvent; 
	}

	public long getTimePoint() {
		return timePoint;
	}

	public void setTimePoint(long timePoint) {
		this.timePoint = timePoint;
	}
	
	public SchedulerNotifierEventListener getListener() {
		return listener;
	}
	
	public void setListener(SchedulerNotifierEventListener listener) {
		this.listener = listener;
	}
	
	public boolean isTerminationEvent() {
		return terminationEvent;
	}

	public void setTerminationEvent(boolean isTerminationEvent) {
		this.terminationEvent = isTerminationEvent;
	}
	
	public boolean equals(EventTime other) {
		return ((this.timePoint == other.timePoint) && this.listener.equals(other.listener));
	}
	
	public int compareTo(EventTime e) {
		if (this.getTimePoint() > e.getTimePoint()) {
			return 1;
		} else if (this.getTimePoint() < e.getTimePoint()) {
			return -1;
		} else {
			if (e.terminationEvent) {
				return -1;
			} else if (this.terminationEvent) {
				return 1;
			}
			return 0;
		} 
	}
	
}
