package cz.cuni.mff.d3s.deeco.demo.cloud;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.jini.TSKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.runtime.NoJPFLauncher;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Main class for launching the application.
 * 
 * @author Michal Kit
 * 
 */
public class TupleSpaceLauncherCloudNoJPF {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Class<?>[] components = { NodeA.class, NodeB.class };
		Class<?>[] ensembles = { MigrationEnsemble.class };
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new TSKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler(km);
		NoJPFLauncher launcher = new NoJPFLauncher(km, scheduler);
		launcher.launch(components, ensembles);
	}
}
