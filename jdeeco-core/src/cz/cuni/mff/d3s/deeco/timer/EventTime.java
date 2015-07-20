package cz.cuni.mff.d3s.deeco.timer;

public class EventTime implements Comparable<EventTime> {
	private long timePoint;
	private TimerEventListener listener;
	private String eventName;
	private boolean terminationEvent;

	public EventTime(long timePoint, TimerEventListener listener, String eventName, boolean isTerminationEvent) {
		this.setTimePoint(timePoint);
		this.listener = listener;
		this.eventName = eventName;
		this.terminationEvent = isTerminationEvent; 
	}

	public long getTimePoint() {
		return timePoint;
	}

	public void setTimePoint(long timePoint) {
		this.timePoint = timePoint;
	}
	
	public TimerEventListener getListener() {
		return listener;
	}
	
	public void setListener(TimerEventListener listener) {
		this.listener = listener;
	}
	
	public String getEventName(){
		return eventName;
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
