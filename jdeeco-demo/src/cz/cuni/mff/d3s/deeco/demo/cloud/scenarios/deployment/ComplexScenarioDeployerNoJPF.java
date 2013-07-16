package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.ClassDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.InitializedDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

public class ComplexScenarioDeployerNoJPF {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// no non-initialized components
		List<Class<?>> components = Arrays
				.asList(new Class<?>[] {});
		List<Class<?>> ensembles = Arrays
				.asList(new Class<?>[] { LinkEnsemble.class });
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler();
		AbstractDEECoObjectProvider dop = new ClassDEECoObjectProvider(
				components, ensembles);
		Runtime rt = new Runtime(km, scheduler);
		rt.registerComponentsAndEnsembles(dop);
	
		List<Component> scpComponents = new ArrayList<Component>( 
			Arrays.asList(
				// 3 SCPis at the LMU Munich 
				new ScpComponent("LMU1", ENetworkId.LMU_MUNICH),
				new ScpComponent("LMU2", ENetworkId.LMU_MUNICH),
				new ScpComponent("LMU3", ENetworkId.LMU_MUNICH),
				new ScpComponent("LMU4", ENetworkId.LMU_MUNICH),
				new ScpComponent("LMU5", ENetworkId.LMU_MUNICH),
				new ScpComponent("LMU6", ENetworkId.LMU_MUNICH),
				new ScpComponent("LMU7", ENetworkId.LMU_MUNICH),
				// 3 SCPis at the IMT Lucca
				new ScpComponent("IMT1", ENetworkId.IMT_LUCCA),
				new ScpComponent("IMT2", ENetworkId.IMT_LUCCA),
				new ScpComponent("IMT3", ENetworkId.IMT_LUCCA),
				new ScpComponent("IMT4", ENetworkId.IMT_LUCCA),
				new ScpComponent("IMT5", ENetworkId.IMT_LUCCA),
				new ScpComponent("IMT6", ENetworkId.IMT_LUCCA),
				new ScpComponent("IMT7", ENetworkId.IMT_LUCCA),
				// 
				new ScpComponent("EGM1", ENetworkId.EN_GARDEN),
				new ScpComponent("EGM2", ENetworkId.EN_GARDEN),
				new ScpComponent("EGM3", ENetworkId.EN_GARDEN))
		);
		// list of all components which are part of the system
		List<Component> cloudComponents = new ArrayList<Component>(scpComponents);	
		// 2 Application Instances to be deployed in the cloud
		cloudComponents.add(new AppComponent("APP1"));
		cloudComponents.add(new AppComponent("APP2"));
		cloudComponents.add(new AppComponent("APP3"));
		cloudComponents.add(new AppComponent("APP4"));
		
		// initialize the singleton of the RandomIntegerDistanceMiddlewareEntry to generate the latency distances
		// these will be dynamically provided by the communication middleware if jDEECo gets connected to it
		Random rand = new Random();
		for (int i = 0; i < scpComponents.size(); i++) {
			ScpComponent c1 = (ScpComponent) scpComponents.get(i);
			// generate the list of distances from one component to the others
			for (int j = 0; j < i; j++){
				// no loop on same index, symetric between source and destination
				if (i != j){
					Long val = null;
					ScpComponent c2 = (ScpComponent) scpComponents.get(j);
					// different treatment for NetworkComponents
					if (!c1.networkId.equals(c2.networkId)){
						val = 150L;
					}else{
						val = ((Integer) rand.nextInt(80)).longValue();
					}
					c1.latencies.put(c2.id, val);
					System.out.println(c1.id + " - " + c2.id + " - " + val);
					c2.latencies.put(c1.id, val);
				}
			}
		}
				
		// initialize the DEECo with input initialized components
		dop = new InitializedDEECoObjectProvider(cloudComponents, null);
		rt.registerComponentsAndEnsembles(dop);
	
		rt.startRuntime();
	}
}
