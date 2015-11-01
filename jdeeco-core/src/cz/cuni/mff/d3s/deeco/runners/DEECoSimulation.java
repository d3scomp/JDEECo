package cz.cuni.mff.d3s.deeco.runners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.simulation.SimulationProvider;

/** 
 * Main entry for launching DEECo simulations.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class DEECoSimulation {
	private int nodeCounter = 0xdec0;
	List<DEECoNode> deecoNodes;
	List<DEECoPlugin> instantiatedPlugins;
	List<Class<? extends DEECoPlugin>> nonInstantiatedPlugins;
	SimulationTimer simulationTimer;
	
	public DEECoSimulation(SimulationProvider simulationProvider) {
		this(simulationProvider.getTimer());
	}

	public DEECoSimulation(SimulationTimer simulationTimer) {
		this.simulationTimer = simulationTimer;
		instantiatedPlugins = new ArrayList<>();
		nonInstantiatedPlugins = new ArrayList<>();
		deecoNodes = new ArrayList<>();
	}
	
	public void addPlugin(Class<? extends DEECoPlugin> clazz) {
		nonInstantiatedPlugins.add(clazz);
	}
	
	public void addPlugin(DEECoPlugin nodeWideplugin) {
		instantiatedPlugins.add(nodeWideplugin);		
	}

	public void start(long duration) {
		simulationTimer.start(duration);
		for(DEECoNode n : deecoNodes){
			n.finalize();
		}
	}
	
	public DEECoNode createNode(DEECoPlugin... nodeSpecificPlugins) throws DEECoException, InstantiationException, IllegalAccessException {
		return createNode(nodeCounter++, nodeSpecificPlugins);
	}
	public DEECoNode createNode(int id, KnowledgeManagerFactory factory, DEECoPlugin... nodeSpecificPlugins) throws DEECoException, InstantiationException, IllegalAccessException {				
		DEECoNode node = new DEECoNode(id, simulationTimer, factory, getPlugins(nodeSpecificPlugins));
		deecoNodes.add(node);
		return node; 
	}

	public DEECoNode createNode(int id, DEECoPlugin... nodeSpecificPlugins) throws DEECoException, InstantiationException, IllegalAccessException {		
		DEECoNode node = new DEECoNode(id, simulationTimer, getPlugins(nodeSpecificPlugins));
		deecoNodes.add(node);
		return node;
	}
	
	private DEECoPlugin [] getPlugins(DEECoPlugin... nodeSpecificPlugins) throws InstantiationException, IllegalAccessException {
		List<DEECoPlugin> plugins = new LinkedList<DEECoPlugin>();
		plugins.addAll(instantiatedPlugins);
		plugins.addAll(Arrays.asList(nodeSpecificPlugins));
		for (Class<? extends DEECoPlugin> c : nonInstantiatedPlugins) {
			plugins.add(c.newInstance());
		}
		return  plugins.toArray(new DEECoPlugin[0]);
	}
	
}
