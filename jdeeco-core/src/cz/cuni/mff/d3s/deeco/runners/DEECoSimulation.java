package cz.cuni.mff.d3s.deeco.runners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;

/** 
 * Main entry for launching DEECo simulations.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class DEECoSimulation {

	List<DEECoNode> deecoNodes;
	List<DEECoPlugin> nodePlugins;
	SimulationTimer simulationTimer;

	public DEECoSimulation(SimulationTimer simulationTimer, DEECoPlugin... nodeWideplugins) {
		this.nodePlugins = Arrays.asList(nodeWideplugins);
		this.simulationTimer = simulationTimer;
		deecoNodes = new ArrayList<>();
	}

	public void start(long duration) {
		simulationTimer.start(duration);
	}

	public DEECoNode createNode(DEECoPlugin... nodeSpecificPlugins) throws DEECoException {
		// Create list of plug-ins for new node
		List<DEECoPlugin> plugins = new LinkedList<DEECoPlugin>();
		plugins.addAll(nodePlugins);
		plugins.addAll(Arrays.asList(nodeSpecificPlugins));
		
		DEECoNode node = new DEECoNode(simulationTimer, plugins.toArray(new DEECoPlugin[0]));
		deecoNodes.add(node);
		return node;
	}
	
}
