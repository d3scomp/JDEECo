package cz.cuni.mff.d3s.deeco.demo.cloud;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.jini.TSKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.InstanceRuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.RealTimeSchedulerJPF;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Main class for launching the application.
 * 
 * @author Michal Kit
 * 
 */
public class TSLauncherCloudNoJPF {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new TSKnowledgeRepository());
		Scheduler scheduler = new RealTimeSchedulerJPF();
		InstanceRuntimeMetadataProvider provider = new InstanceRuntimeMetadataProvider();
		provider.fromComponentInstance(new NodeA());
		provider.fromComponentInstance(new NodeB());
		provider.fromEnsembleDefinition(MigrationEnsemble.class);
		Runtime rt = new Runtime(scheduler, km);
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());
		rt.run();
	}
}
