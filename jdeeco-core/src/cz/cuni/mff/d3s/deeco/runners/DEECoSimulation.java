package cz.cuni.mff.d3s.deeco.runners;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.notifier.SimulationSchedulerNotifier;

/** 
 * Main entry for launching DEECo simulations.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class DEECoSimulation implements DEECoSimulationRunner {

	List<DEECoNode> deecoNodes;
	DEECoPlugin[] nodeWideplugins;
	
	SimulationSchedulerNotifier simulationSchedulerNotifier;
	boolean terminationTimeSet = false;

	public DEECoSimulation(SimulationSchedulerNotifier simulationSchedulerNotifier, DEECoPlugin... nodeWideplugins) {
		this.nodeWideplugins = nodeWideplugins;
		this.simulationSchedulerNotifier = simulationSchedulerNotifier;
		deecoNodes = new ArrayList<>();
	}

	@Override
	public void start() throws TerminationTimeNotSetException {
		if (!terminationTimeSet) {
			throw new TerminationTimeNotSetException(
					"Before starting a simulation you have to specify the termination time.");
		}
		simulationSchedulerNotifier.start();
	}

	@Override
	public DEECoNode createNode(DEECoPlugin... nodeSpecificPlugins) throws DEECoException {
		DEECoNode node = new DEECoNode(simulationSchedulerNotifier, DEECoRun.getAllPlugins(nodeWideplugins, nodeSpecificPlugins));
		deecoNodes.add(node);
		return node;
	}
	
	@Override
	public void setTerminationTime(long terminationTime) {
		simulationSchedulerNotifier.setTerminationTime(terminationTime);
		terminationTimeSet  = true;
	}
	
}
