package cz.cuni.mff.d3s.deeco.runners;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.notifier.SchedulerNotifier;

/**
 * Main entry for running DEECo in "real" deployment.
 * For simulation purposes use {@link DEECoSimulation} instead.
 * 
 * <p>
 * Factory for {@link DEECoNode}.
 * </p>
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class DEECoRun implements DEECoRunner {

	List<DEECoNode> deecoNodes;
	DEECoPlugin[] nodeWideplugins;
	
	SchedulerNotifier schedulerNotifier;
	
	public DEECoRun(SchedulerNotifier schedulerNotifier, DEECoPlugin... nodeWideplugins) {
		this.nodeWideplugins = nodeWideplugins;
		this.schedulerNotifier = schedulerNotifier;
		deecoNodes = new ArrayList<>();
	}
	
	@Override
	public void start() {
		schedulerNotifier.start();
	}

	@Override
	public DEECoNode createNode(DEECoPlugin... nodeSpecificPlugins) throws DEECoException {
		DEECoNode node = new DEECoNode(schedulerNotifier, getAllPlugins(nodeWideplugins, nodeSpecificPlugins));
		deecoNodes.add(node);
		return node;
	}
	
	/**
	 * Helper method that concatenates the array of node-wide plugins with node-specific plugins.
	 * The returned array is intended to be passed to the DEECoNode() constructor.    
	 * @param nodeSpecificPlugins extra plugins, not provided by the factory
	 */
	static DEECoPlugin[] getAllPlugins(DEECoPlugin[] nodeWideplugins, DEECoPlugin[] nodeSpecificPlugins) {
		DEECoPlugin[] ret = new DEECoPlugin[nodeSpecificPlugins.length+nodeWideplugins.length];
		int ind=0;
		for (int i=0; i<nodeSpecificPlugins.length; i++) {
			ret[ind] = nodeSpecificPlugins[i];
			ind++;
		}
		for (int j=0; j<nodeWideplugins.length; j++) {
			ret[ind] = nodeSpecificPlugins[j];
			ind++;			
		}
		return ret;
	}
}
