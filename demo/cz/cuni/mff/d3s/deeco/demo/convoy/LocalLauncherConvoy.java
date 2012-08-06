package cz.cuni.mff.d3s.deeco.demo.convoy;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.runtime.NoJPFLauncher;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Main class for launching the application.
 * 
 * @author Michal Kit
 * 
 */
public class LocalLauncherConvoy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Class<?>[] components = { RobotLeaderComponent.class, RobotFollowerComponent.class};
		Class<?>[] ensembles = {ConvoyEnsemble.class};
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler(km);
		NoJPFLauncher launcher = new NoJPFLauncher(km, scheduler);
		launcher.launch(components, ensembles);
	}
}
