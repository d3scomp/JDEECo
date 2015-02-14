package cz.cuni.mff.d3s.deeco.scheduler.notifier;

import java.util.PriorityQueue;
import java.util.Queue;

public class DiscreteEventSchedulerNotifier implements SimulationSchedulerNotifier {
	
	protected Queue<EventTime> eventTimes;
	
	protected  long currentTime;
	
	public DiscreteEventSchedulerNotifier() {
		currentTime = 0;
		eventTimes = new PriorityQueue<>();
	}
	
	@Override
	public long getCurrentMilliseconds() {
		return currentTime;
	}
	
	public void notifyAt(long time, SchedulerNotifierEventListener listener) {
		EventTime eventTime = new EventTime(time, listener, false);
		if (!eventTimes.contains(eventTime)) {
			eventTimes.add(eventTime);
		}
	}
	
	@Override
	public void start() {
		
		while (!tryToTerminate()) {
			EventTime eventTime = eventTimes.remove();
			currentTime = eventTime.getTimePoint();
			eventTime.getListener().at(currentTime);
		}
	}

	private boolean tryToTerminate() {
		if (eventTimes.isEmpty()) {
			return true;
		} 
		EventTime eventTime = eventTimes.peek();
		if (!eventTime.isTerminationEvent()) {
			return false;
		} 
		currentTime = eventTime.getTimePoint();
		eventTimes.clear();
		return true;
	}

	@Override 
	public void setTerminationTime(long terminationTime) {
		eventTimes.add(new EventTime(terminationTime, new SchedulerNotifierEventListener(){
			@Override
			public void at(long time) {
				// termination time reached, do nothing
			}}, true));
	}

	@Override
	public void reset() {
		currentTime = 0;
		eventTimes.clear();
	}

}
