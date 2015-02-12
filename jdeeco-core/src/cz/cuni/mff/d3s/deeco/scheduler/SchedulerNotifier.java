package cz.cuni.mff.d3s.deeco.scheduler;

public interface SchedulerNotifier extends CurrentTimeProvider, TerminationTimeHolder {

	void notifyAt(long time);

	void setSimulationTimeEventListener(SimulationTimeEventListener simulationTimeEventListener);
	
	void nextStep();
	
	boolean tryToTerminate();
	
	/**
	 * This call sets the current time of the simulator to 0. 
	 */
	void reset();

}