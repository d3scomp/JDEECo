package cz.cuni.mff.d3s.deeco.demo.cloud;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.DEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.IScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.discrete.DiscreteScheduler;

/**
 * Main class for launching the application.
 * 
 * @author Michal Kit
 * 
 */
public class LocalLauncherCloudNoJPFDiscreteScheduler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Class<?>> components = Arrays
				.asList(new Class<?>[] { NodeB.class, NodeD.class });
		List<Class<?>> ensembles = Arrays
				.asList(new Class<?>[] { MigrationEnsemble.class });
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		IScheduler scheduler = new DiscreteScheduler();
		DEECoObjectProvider dop = new DEECoObjectProvider(
				components, ensembles);
		Runtime rt = new Runtime(km, scheduler);
		rt.registerComponentsAndEnsembles(dop);

		dop = new DEECoObjectProvider();
		dop.addInitialKnowledge(new NodeA("NodeA", .5f, 1));
		rt.registerComponentsAndEnsembles(dop);

		rt.startRuntime();
	}
}
