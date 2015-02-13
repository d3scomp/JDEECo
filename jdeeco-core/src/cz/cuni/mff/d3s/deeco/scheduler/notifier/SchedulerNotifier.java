package cz.cuni.mff.d3s.deeco.scheduler.notifier;


public interface SchedulerNotifier extends CurrentTimeProvider {

	void notifyAt(long time, SchedulerNotifierEventListener listener);

	void start();
	
}