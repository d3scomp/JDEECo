package cz.cuni.mff.d3s.deeco.scheduler.notifier;


public class WallTimeSchedulerNotifier extends DiscreteEventSchedulerNotifier {

	long terminationTime;
	
	public void start() {
		
		while (!tryToTerminate()) {
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
	
	public boolean tryToTerminate() {
		if (currentTime < terminationTime) {
			return false;
		}
		eventTimes.clear();
		return true;
	}
	
	public void setTerminationTime(long terminationTime) {
		this.terminationTime = terminationTime;
	}
}
