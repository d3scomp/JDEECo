package example1;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Population;

import tutorial.matsim.PopulationAgentSource;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.jdeeco.matsim.MATSimSimulation;

public class Main {

	private static final String MATSIM_CONFIG_CUSTOM = "input/config.xml";
	
	/*private static JDEECoAgentSource jdeecoAgentSource;
	private static MATSimSimulation simulation;
	private static MATSimRouter router;
	private static MATSimDataProviderReceiver matSimProviderReceiver;
	
	private static AnnotationProcessor processor;
	private static SimulationRuntimeBuilder builder;
*/
	public static void main(String[] args) throws AnnotationProcessorException,	IOException {
		Log.i("Preparing simulation");
		
		PopulationAgentSource populationAgentSource = new PopulationAgentSource();
		
		MATSimSimulation simulation = new MATSimSimulation(new File(MATSIM_CONFIG_CUSTOM), populationAgentSource);
		
//		reducePopulation(simulation.getControler().getPopulation());
//		populationAgentSource.setPopulation(simulation.getControler().getPopulation());

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
	
	private static void createAndDeployVehicleComponent(int idx, String sourceLinkIdString) throws AnnotationProcessorException {
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
