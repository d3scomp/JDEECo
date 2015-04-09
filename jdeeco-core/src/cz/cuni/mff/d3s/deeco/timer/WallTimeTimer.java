package cz.cuni.mff.d3s.deeco.timer;

import java.util.PriorityQueue;
import java.util.Queue;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;


public class WallTimeTimer implements RunnerTimer {

	Queue<EventTime> eventTimes;
	long currentTime;
	
	public WallTimeTimer() {
		currentTime = 0;
		eventTimes = new PriorityQueue<>();
	}
	
	@Override
	public long getCurrentMilliseconds() {
		return currentTime;
	}
	
	public void start() {
		
		while (true) {
			EventTime eventTime = eventTimes.remove();
			long nextEventTime = eventTime.getTimePoint();
			try {
				Thread.sleep(nextEventTime-currentTime);
				currentTime = nextEventTime;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			eventTime.getListener().at(currentTime);
		}
	}

	@Override
	public void notifyAt(long time, TimerEventListener listener, DEECoContainer container) {
		EventTime eventTime = new EventTime(time, listener, false);
		if (!eventTimes.contains(eventTime)) {
			eventTimes.add(eventTime);
		}
	}
}
