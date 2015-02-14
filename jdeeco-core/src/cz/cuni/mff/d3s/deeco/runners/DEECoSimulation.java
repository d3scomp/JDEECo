package cz.cuni.mff.d3s.deeco.runners;

import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.notifier.SimulationSchedulerNotifier;

/** 
 * Main class for launching DEECo simulations.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class DEECoSimulation extends DEECoRun {

	private boolean terminationTimeSet = false;

	public DEECoSimulation(SimulationSchedulerNotifier simulationSchedulerNotifier, DEECoPlugin... simulationWideplugins) {
		super(simulationSchedulerNotifier, simulationWideplugins);
	}

	public void setTerminationTime(long terminationTime) {
		simulationSchedulerNotifier.setTerminationTime(terminationTime);
		terminationTimeSet  = true;
	}

	public void start() throws TerminationTimeNotSetException {
		if (!terminationTimeSet) {
			throw new TerminationTimeNotSetException(
					"Before starting a simulation you have to specify the termination time.");
		}
		simulationSchedulerNotifier.start();
	}
	
}
