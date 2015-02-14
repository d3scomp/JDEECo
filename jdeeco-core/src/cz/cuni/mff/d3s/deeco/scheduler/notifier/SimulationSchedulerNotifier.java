package cz.cuni.mff.d3s.deeco.scheduler.notifier;


public interface SimulationSchedulerNotifier extends SchedulerNotifier {

	public void setTerminationTime(long terminationTime);
	
	/**
	 * This call sets the current time of the simulation to 0 and clears the existing time events. 
	 */
	public void reset();
	
}
