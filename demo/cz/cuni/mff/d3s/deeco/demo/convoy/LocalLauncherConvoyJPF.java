package cz.cuni.mff.d3s.deeco.demo.convoy;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.processor.ClassProcessor;
import cz.cuni.mff.d3s.deeco.provider.FileDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Launcher;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
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
		ClassProcessor.main(args);
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler(km);
		Launcher launcher = new Launcher(scheduler, new FileDEECoObjectProvider());
		launcher.launch(); 
	}
}
