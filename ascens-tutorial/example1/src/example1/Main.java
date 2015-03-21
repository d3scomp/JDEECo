package example1;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.basic.v01.IdImpl;

import tutorial.matsim.PopulationAgentSource;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
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
	
	/*private static JDEECoAgentSource jdeecoAgentSource;
	private static MATSimSimulation simulation;
	private static MATSimRouter router;
	private static MATSimDataProviderReceiver matSimProviderReceiver;
	
	private static AnnotationProcessor processor;
	private static SimulationRuntimeBuilder builder;
*/
	public static void main(String[] args) throws AnnotationProcessorException,	IOException, InstantiationException, IllegalAccessException, DEECoException {
		Log.i("Preparing simulation");
		
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
		
		
		Log.i("Creating components");
		
		createAndDeployVehicleComponent(1, "1_1");
		createAndDeployVehicleComponent(2, "50_2");
		createAndDeployVehicleComponent(3, "22_3");
		createAndDeployVehicleComponent(4, "59_3");
		
		// Overrides end time specified in the MATSim configuration
		simulation.start(2900000);
		
		
	/*	jdeecoAgentSource = new JDEECoAgentSource();
		PopulationAgentSource populationAgentSource = new PopulationAgentSource();
		
		matSimProviderReceiver = new MATSimDataProviderReceiver();
		simulation = new MATSimSimulation(matSimProviderReceiver, matSimProviderReceiver, Arrays.asList(jdeecoAgentSource, populationAgentSource), MATSIM_CONFIG_CUSTOM);
		reducePopulation(simulation.getControler().getPopulation());
		populationAgentSource.setPopulation(simulation.getControler().getPopulation());
		
		router = new MATSimRouter(simulation.getControler(), simulation.getTravelTime());
		matSimProviderReceiver.setRouter(router);

		Log.i("Creating components");

		processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE);
		builder = new SimulationRuntimeBuilder();

		createAndDeployVehicleComponent(1, "1_1");
		createAndDeployVehicleComponent(2, "50_2");
		createAndDeployVehicleComponent(3, "22_3");
		createAndDeployVehicleComponent(4, "59_3");		
		
		simulation.run();*/
		Log.i("Simulation Finished");
	}
	
	private static void createAndDeployVehicleComponent(int idx, String sourceLinkIdString) throws AnnotationProcessorException, InstantiationException, IllegalAccessException, DEECoException {
		String compIdString = "V" + idx;
		Id compId = new IdImpl(compIdString);
		Id sourceLinkId = new IdImpl(sourceLinkIdString);
		
		MATSimVehicle agent = new MATSimVehicle(sourceLinkId); // MATSim agent with start position
		DEECoNode node = simulation.createNode(idx, agent); // DEECO node with Id and agent as plug-in
		
		VehicleComponent component = new VehicleComponent(compIdString, 
				agent.getActuatorProvider(), agent.getSensorProvider(), matSim.getRouter(), agent.getSimulation().getTimer());
		node.deployComponent(component);
		
		
	/*	String compIdString = "V" + idx;
		Id compId = new IdImpl(compIdString);
		Id sourceLinkId = new IdImpl(sourceLinkIdString);

		jdeecoAgentSource.addAgent(new JDEECoAgent(compId, sourceLinkId));

		VehicleComponent component = new VehicleComponent(compIdString, 
				matSimProviderReceiver.getActuatorProvider(compId), matSimProviderReceiver.getSensorProvider(compId), router, simulation);
		
		RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		processor.process(model, component);
		
		DirectSimulationHost host = simulation.getHost(compIdString);
		RuntimeFramework runtime = builder.build(host, simulation, model, null, null);
		runtime.start(); */		
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
