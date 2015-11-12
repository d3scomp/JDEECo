package cz.cuni.mff.d3s.deeco.timer;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;

public class DiscreteEventTimer extends BaseTimer implements SimulationTimer {

	private static final String TERMINATION_EVENT_NAME = "termination_event";
	
	Queue<EventTime> eventTimes;
	Map<DEECoContainer, EventTime> containerEvents;
	long currentTime;

	public DiscreteEventTimer() {
		currentTime = 0;
		eventTimes = new PriorityQueue<>();
		containerEvents = new HashMap<>();
	}

	@Override
	public long getCurrentMilliseconds() {
		return currentTime;
	}

	@Override
	public void start(long duration) {
		runStartupListeners();

		eventTimes.add(new EventTime(duration, new TimerEventListener() {
			@Override
			public void at(long time) {
				// termination time reached, do nothing
			}
		}, TERMINATION_EVENT_NAME, true));

		while (!tryToTerminate()) {
			EventTime eventTime = eventTimes.remove();
			currentTime = eventTime.getTimePoint();
			eventTime.getListener().at(currentTime);
		}
		
		runShutdownListeners();
	}

	/**
	 * Set notification event for container
	 * 
	 * NOTE: Only one event per container is registered
	 */
	@Override
	public void notifyAt(long time, TimerEventListener listener, String eventName, DEECoContainer container) {
		EventTime eventTime = new EventTime(time, listener, eventName, false);
		if (!eventTimes.contains(eventTime)) {
			// Replace old event for container by the new one
			eventTimes.add(eventTime);
			EventTime old = containerEvents.get(container);
			if (old != null) {
				eventTimes.remove(old);
			}
			containerEvents.put(container, eventTime);
		}
	}

	@Override
	public void interruptionEvent(TimerEventListener listener, String eventName, DEECoContainer container){
		throw new UnsupportedOperationException();
		// If you are about to implement this method remember to use synchronization
	}

	boolean tryToTerminate() {
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

}