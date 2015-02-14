package cz.cuni.mff.d3s.deeco.runners;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.notifier.SimulationSchedulerNotifier;

/**
 * Main class for running DEECo in "real" deployment.
 * For simulation purposes use {@link DEECoSimulation} instead.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class DEECoRun {

	List<DEECoNode> nodesList;
	DEECoPlugin[] simulationWideplugins;
	SimulationSchedulerNotifier simulationSchedulerNotifier;
	
	public DEECoRun(SimulationSchedulerNotifier simulationSchedulerNotifier, DEECoPlugin... simulationWideplugins) {
		this.simulationWideplugins = simulationWideplugins;
		this.simulationSchedulerNotifier = simulationSchedulerNotifier;
		nodesList = new ArrayList<>();
	}
	
	public void start() throws TerminationTimeNotSetException {
		simulationSchedulerNotifier.start();
	}

	public DEECoNode createNode(DEECoPlugin... extraPlugins) throws DEECoException {
		DEECoNode node = new DEECoNode(simulationSchedulerNotifier, getAllPlugins(extraPlugins));
		nodesList.add(node);
		return node;
	}
	
	/**
	 * Concatenates the array of simulation-wide plugins with node-specific plugins.
	 * The returned array is intended to be passed to the DEECoNode() constructor.    
	 * @param extraPlugins node-specific plugins
	 */
	private DEECoPlugin[] getAllPlugins(DEECoPlugin[] extraPlugins) {
		DEECoPlugin[] ret = new DEECoPlugin[extraPlugins.length+simulationWideplugins.length];
		int ind=0;
		for (int i=0; i<extraPlugins.length; i++) {
			ret[ind] = extraPlugins[i];
			ind++;
		}
		for (int j=0; j<simulationWideplugins.length; j++) {
			ret[ind] = extraPlugins[j];
			ind++;			
		}
		return ret;
	}
}
