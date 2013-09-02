package cz.cuni.mff.d3s.deeco.demo.cloud;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.InstanceRuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.DiscreteScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

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
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new DiscreteScheduler();
		InstanceRuntimeMetadataProvider provider = new InstanceRuntimeMetadataProvider();
		provider.fromComponentInstance(new NodeB());
		provider.fromComponentInstance(new NodeD());
		provider.fromComponentInstance(new NodeA("NodeA", .5f, 1));
		provider.fromEnsembleDefinition(MigrationEnsemble.class);
		Runtime rt = new Runtime(scheduler, km);
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());

		rt.run();
	}
}
