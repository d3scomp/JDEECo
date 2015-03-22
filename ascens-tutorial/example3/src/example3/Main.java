package example3;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.basic.v01.IdImpl;

import tutorial.matsim.PopulationAgentSource;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.jdeeco.matsim.MATSimSimulation;
import cz.cuni.mff.d3s.jdeeco.matsim.MATSimVehicle;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.BroadcastLoopback;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

public class Main {

	private static final String MATSIM_CONFIG_CUSTOM = "input/config.xml";
	
	private static MATSimSimulation matSim;
	private static DEECoSimulation simulation; 

	private static Random random = new Random(329884L);
	
	public static void main(String[] args) throws AnnotationProcessorException,	IOException, InstantiationException, IllegalAccessException, DEECoException {
		System.out.println("Preparing simulation...");
		
		PopulationAgentSource populationAgentSource = new PopulationAgentSource();
		
		matSim = new MATSimSimulation(new File(MATSIM_CONFIG_CUSTOM), populationAgentSource);
		
		reducePopulation(matSim.getController().getPopulation());
		populationAgentSource.setPopulation(matSim.getController().getPopulation());
		
		simulation = new DEECoSimulation(matSim.getTimer());
		// Add MATSim plug-in for all nodes
		simulation.addPlugin(matSim);
		
		// Configure loop-back networking for all nodes
		simulation.addPlugin(new BroadcastLoopback());
		simulation.addPlugin(Network.class);
		simulation.addPlugin(DefaultKnowledgePublisher.class);
		simulation.addPlugin(KnowledgeInsertingStrategy.class);
		
		
		System.out.println("Creating components...");
		
		for (int i=1; i <= 20; i++) {
			createAndDeployVehicleComponent(i, getRandomLink().toString(), "22_3");
		}
		
		// Overrides end time specified in the MATSim configuration
		simulation.start(1200000);
		
		System.out.println("Simulation Finished!");
	}
	
	private static void createAndDeployVehicleComponent(int idx, String sourceLinkIdString, String destLinkIdString) throws AnnotationProcessorException, InstantiationException, IllegalAccessException, DEECoException {
		String compIdString = "V" + idx;
		Id sourceLinkId = new IdImpl(sourceLinkIdString);
		
		MATSimVehicle agent = new MATSimVehicle(sourceLinkId); // MATSim agent with start position
		DEECoNode node = simulation.createNode(idx, agent); // DEECO node with Id and agent as plug-in
		
		VehicleComponent component = new VehicleComponent(compIdString, new IdImpl(destLinkIdString),
				agent.getActuatorProvider(), agent.getSensorProvider(), matSim.getRouter(), agent.getSimulation().getTimer());
		node.deployComponent(component);
		node.deployEnsemble(CapacityExchangeEnsemble.class);
	}
	
	private static Id getRandomLink() {
		Id result = null;
		Set<Id> linkIds = matSim.getRouter().getLinks().keySet();			

		int nth = random.nextInt(linkIds.size());
		for (Iterator<Id> iter = linkIds.iterator(); nth>=0; nth--) {
			result = iter.next();
		}

		return result;
	}

	/**
	 * This method reduces the original population. This is for didactic purposes - it decreases the traffic jams
	 * and makes the visualization of our components more informative. 
	 */
	private static void reducePopulation(Population population) {
		Iterator<Id> personIter = population.getPersons().keySet().iterator();
		int counter = 0;
		while (personIter.hasNext()) {
			personIter.next();
			
			if (counter % 4 != 0) {
				personIter.remove();
			}
			
			counter++;
		}		
	}

}
