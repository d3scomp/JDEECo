package cz.cuni.mff.d3s.deeco.scheduler;

import java.util.PriorityQueue;
import java.util.Queue;

public class DiscreteEventSimulation implements SchedulerNotifier {
	
	Queue<EventTime> eventTimes;
	SimulationTimeEventListener simulationTimeEventListener;
	
	long currentTime;
	
	public DiscreteEventSimulation() {
		currentTime = 0;
		eventTimes = new PriorityQueue<>();
	}
	
	class EventTime implements Comparable<EventTime> {
		long timePoint;
		boolean isTerminationEvent;

		public EventTime(long timePoint, boolean isTerminationEvent) {
			this.timePoint = timePoint;
			this.isTerminationEvent = isTerminationEvent; 
		}

		public int compareTo(EventTime e) {
			if (this.timePoint > e.timePoint) {
				return 1;
			} else if (this.timePoint < e.timePoint) {
				return -1;
			} else {
				if (e.isTerminationEvent) {
					return -1;
				} else if (this.isTerminationEvent) {
					return 1;
				}
				return 0;
			} 
		}		
	}

	@Override
	public long getCurrentMilliseconds() {
		return currentTime;
	}

	@Override
	public void setSimulationTimeEventListener(SimulationTimeEventListener simulationTimeEventListener) {
		this.simulationTimeEventListener = simulationTimeEventListener;
	}
	
	@Override
	public void notifyAt(long time) {
		eventTimes.add(new EventTime(time,false));
	}
	
	@Override
	public boolean tryToTerminate() {
		if (!eventTimes.isEmpty()) {
			EventTime eventTime = eventTimes.peek();
			if (eventTime.isTerminationEvent) {
				currentTime = eventTime.timePoint;
				eventTimes.clear();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void nextStep() {
		if (!eventTimes.isEmpty()) {
			currentTime = eventTimes.remove().timePoint;
			simulationTimeEventListener.at(currentTime);
		}
	}

	@Override
	public void setTerminationTime(long duration) {
		eventTimes.add(new EventTime(duration,true));
	}

	@Override
	public void reset() {
		currentTime = 0;
		eventTimes.clear();
	}

}
