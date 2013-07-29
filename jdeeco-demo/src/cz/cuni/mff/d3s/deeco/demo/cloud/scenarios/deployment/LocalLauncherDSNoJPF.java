package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.ENetworkId;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.LatencyGenerator;
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

/**
 * The Science Cloud Platform (Scp) local launcher for the Deployment Scenario (DS).
 * 
 * @author Julien Malvot
 * 
 */
public class LocalLauncherDSNoJPF {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// no non-initialized components
		List<Class<?>> components = Arrays
				.asList(new Class<?>[] {});
		List<Class<?>> ensembles = Arrays
				.asList(new Class<?>[] { DeployDSEnsemble.class });
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
				new ScpDSComponent("LMU1", ENetworkId.LMU_MUNICH),
				new ScpDSComponent("LMU2", ENetworkId.LMU_MUNICH),
				new ScpDSComponent("LMU3", ENetworkId.LMU_MUNICH),
				// 3 SCPis at the IMT Lucca
				new ScpDSComponent("IMT1", ENetworkId.IMT_LUCCA),
				new ScpDSComponent("IMT2", ENetworkId.IMT_LUCCA),
				new ScpDSComponent("IMT3", ENetworkId.IMT_LUCCA),
				new ScpDSComponent("EGM1", ENetworkId.EN_GARDEN))
		);
		// list of all components which are part of the system
		List<Component> cloudComponents = new ArrayList<Component>(scpComponents);	
		// 2 Application Instances to be deployed in the cloud
		cloudComponents.add(new AppDSComponent("APP1"));
		cloudComponents.add(new AppDSComponent("APP2"));
		// generate the latencies on the scp components
		LatencyGenerator.generate(scpComponents, true);	
		// initialize the DEECo with input initialized components
		dop = new InitializedDEECoObjectProvider(cloudComponents, null);
		rt.registerComponentsAndEnsembles(dop);
	
		rt.startRuntime();
	}
}
