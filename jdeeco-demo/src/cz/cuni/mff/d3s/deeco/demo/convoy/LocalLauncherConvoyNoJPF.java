package cz.cuni.mff.d3s.deeco.demo.convoy;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.model.provider.InstanceRuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.RealTimeSchedulerJPF;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Main class for launching the application.
 * 
 * @author Michal Kit
 * 
 */
public class LocalLauncherConvoyNoJPF {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KnowledgeRepository kr = new LocalKnowledgeRepository();
		KnowledgeManager km = new RepositoryKnowledgeManager(kr);
		Scheduler scheduler = new RealTimeSchedulerJPF(km);
		kr.setTimeProvider(scheduler);
		InstanceRuntimeMetadataProvider provider = new InstanceRuntimeMetadataProvider();
		provider.fromComponentInstance(new RobotLeaderComponent());
		provider.fromComponentInstance(new RobotFollowerComponent());
		provider.fromEnsembleDefinition(ConvoyEnsemble.class);
		Runtime rt = new Runtime(scheduler, km);
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());
		rt.run();
	}
}
