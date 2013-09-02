package cz.cuni.mff.d3s.deeco.demo.convoy;

import cz.cuni.mff.d3s.deeco.jpf.ParsedObjectReader;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.RuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.RealTimeSchedulerJPF;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Main class for launching the application.
 * 
 * @author Michal Kit
 * 
 */
public class LocalLauncherConvoyJPF {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// PreprocessorLauncher.main(args);
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new RealTimeSchedulerJPF();
		RuntimeMetadataProvider provider = new ParsedObjectReader().read();
		Runtime rt = new Runtime(scheduler, km);
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());
		rt.run();
	}
}
