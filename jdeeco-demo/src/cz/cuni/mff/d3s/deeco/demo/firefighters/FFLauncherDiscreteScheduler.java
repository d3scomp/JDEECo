package cz.cuni.mff.d3s.deeco.demo.firefighters;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.InstanceRuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.DiscreteScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Main class for launching the FF scenario demo - for Discrete Event
 * Simulations
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class FFLauncherDiscreteScheduler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scheduler scheduler = new DiscreteScheduler();
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository(scheduler));
		InstanceRuntimeMetadataProvider provider = new InstanceRuntimeMetadataProvider();
		provider.fromEnsembleDefinition(SensorDataAggregation.class);
		provider.fromEnsembleDefinition(CriticalDataAggregation.class);
		provider.fromComponentInstance(new GroupMember("FF1", "T1"));
		provider.fromComponentInstance(new GroupMember("FF2", "T1"));
		provider.fromComponentInstance(new GroupMember("FF3", "T1"));
		provider.fromComponentInstance(new GroupMember("FF4", "T2"));
		provider.fromComponentInstance(new GroupMember("FF5", "T2"));
		provider.fromComponentInstance(new GroupMember("FF6", "T2"));
		provider.fromComponentInstance(new GroupLeader("GL1", "T1", true,
				new Position(0, 0)));
		provider.fromComponentInstance(new GroupLeader("GL2", "T2", false,
				new Position(1000, 1000)));
		Runtime rt = new Runtime(scheduler, km);
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());
		rt.run();
	}
}
