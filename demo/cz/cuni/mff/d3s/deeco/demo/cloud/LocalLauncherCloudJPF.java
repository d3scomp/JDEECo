package cz.cuni.mff.d3s.deeco.demo.cloud;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.PreLauncherDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Launcher;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedSchedulerJPF;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

public class LocalLauncherCloudJPF {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//ClassProcessor.main(args);
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedSchedulerJPF();
		AbstractDEECoObjectProvider dop = new PreLauncherDEECoObjectProvider();
		dop.setKnowledgeManager(km);
		Launcher launcher = new Launcher(scheduler, new PreLauncherDEECoObjectProvider());
		launcher.launch(); 
	}
}
