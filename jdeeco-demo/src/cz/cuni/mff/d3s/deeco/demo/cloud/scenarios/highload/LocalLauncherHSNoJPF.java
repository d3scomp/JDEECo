package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.highload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.ENetworkId;
import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.ClassDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.InitializedDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.DynamicRuntime;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * The Science Cloud Platform (Scp) local launcher for the Highload Scenario (HS).
 * 
 * The launcher retains a static instance of the dynamic runtime enabling the developer
 * to spawn a new scp component if the one in charge of the singleton gets overloaded.
 * 
 * @author Julien Malvot
 * 
 */
public class LocalLauncherHSNoJPF {

	public static DynamicRuntime dynamicRuntime = null;
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// no non-initialized components
		List<Class<?>> components = Arrays.asList(new Class<?>[] {});
		List<Class<?>> ensembles = Arrays
				.asList(new Class<?>[] { BalanceHSEnsemble.class });
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler();
		AbstractDEECoObjectProvider dop = new ClassDEECoObjectProvider(
				components, ensembles);
		// the dynamic runtime enables the developper to register/unregister components at runtime
		dynamicRuntime = new DynamicRuntime(km, scheduler);
		dynamicRuntime.registerComponentsAndEnsembles(dop);

		AppHSComponent appComponent = new AppHSComponent("APP", "machine", "IMT1", true);
		ArrayList<ScpHSComponent> scpComponents = new ArrayList<ScpHSComponent>(Arrays.asList(
				// 3 SCPis at the LMU Munich
				new ScpHSComponent("LMU1", ENetworkId.LMU_MUNICH),
				new ScpHSComponent("LMU2", ENetworkId.LMU_MUNICH),
				new ScpHSComponent("LMU3", ENetworkId.LMU_MUNICH),
				// 3 SCPis at the IMT Lucca
				new ScpHSComponent("IMT1", ENetworkId.IMT_LUCCA, Arrays.asList(appComponent.id) ),
				new ScpHSComponent("IMT2", ENetworkId.IMT_LUCCA),
				new ScpHSComponent("IMT3", ENetworkId.IMT_LUCCA),

				new ScpHSComponent("EGM1", ENetworkId.EN_GARDEN)));
		// list of all components which are part of the system
		List<Component> cloudComponents = new ArrayList<Component>(
				scpComponents);
		// Singleton Instance experiencing high load with a machine running on IMT Lucca
		cloudComponents.add(appComponent);

		// initialize the DEECo with input initialized components
		dop = new InitializedDEECoObjectProvider(cloudComponents, null);
		dynamicRuntime.registerComponentsAndEnsembles(dop);
		dynamicRuntime.startRuntime();
	}
}
