package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.shutdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.ENetworkId;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.LatencyGenerator;
import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.DEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * The Science Cloud Platform (Scp) local launcher for the Shutdown Scenario (SS).
 * 
 * @author Julien Malvot
 * 
 */
public class LocalLauncherSSNoJPF {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler();
		Runtime rt = new Runtime(km, scheduler);
		List<Class<?>> ensembles = Arrays.asList(new Class<?>[] { 
				DeploySSEnsemble.class,
				BackupSSEnsemble.class, 
				MonitorSSEnsemble.class
		});
		
		List<Component> scpComponents = new ArrayList<Component>(Arrays.asList(
				// 3 SCPis at the LMU Munich
				new ScpSSComponent("LMU1", ENetworkId.LMU_MUNICH, Arrays.asList(2400, 2400)),
				new ScpSSComponent("LMU2", ENetworkId.LMU_MUNICH, Arrays.asList(2200, 2200)),
				new ScpSSComponent("LMU3", ENetworkId.LMU_MUNICH, Arrays.asList(1600, 1600)),
				// 3 SCPis at the IMT Lucca
				new ScpSSComponent("IMT1", ENetworkId.IMT_LUCCA, Arrays.asList(2400, 2400)),
				new ScpSSComponent("IMT2", ENetworkId.IMT_LUCCA, Arrays.asList(2200, 2200)),
				new ScpSSComponent("IMT3", ENetworkId.IMT_LUCCA, Arrays.asList(1600, 1600)),
				// 1 SCPi in the English Garden (mobile device)
				new ScpSSComponent("EGM1", ENetworkId.EN_GARDEN, Arrays.asList(800)))
		);
		// list of all components which are part of the system
		List<Component> cloudComponents = new ArrayList<Component>(
				scpComponents);
		// Singleton Instance experiencing high load with a machine running on IMT Lucca
		cloudComponents.add(new AppSSComponent("APP", "machine"));
		// generate the latencies on the scp components
		LatencyGenerator.generate(scpComponents, true);
		// initialize the DEECo with input initialized components
		DEECoObjectProvider dop = new DEECoObjectProvider();
		dop.addEnsembles(ensembles);
		dop.addInitialKnowledge(cloudComponents);
		rt.registerComponentsAndEnsembles(dop);
		
		rt.startRuntime();
	}
}
