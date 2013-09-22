package cz.cuni.mff.d3s.deeco.demo.cloud;

import cz.cuni.mff.d3s.deeco.jpf.ParsedObjectReader;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.RuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.RealTimeSchedulerJPF;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

public class LocalLauncherCloudJPF {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scheduler scheduler = new RealTimeSchedulerJPF();
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository(scheduler));
		RuntimeMetadataProvider provider = new ParsedObjectReader().read();
		Runtime rt = new Runtime(scheduler, km);
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());
		rt.run();
	}
}
