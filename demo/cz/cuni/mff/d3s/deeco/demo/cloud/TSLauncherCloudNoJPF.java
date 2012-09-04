package cz.cuni.mff.d3s.deeco.demo.cloud;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.jini.TSKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.ClassDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Launcher;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
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
		List<Class<?>> components = Arrays.asList(new Class<?>[]{ NodeA.class, NodeB.class });
		List<Class<?>> ensembles = Arrays.asList(new Class<?>[]{ MigrationEnsemble.class });
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new TSKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler(km);
		Launcher launcher = new Launcher(scheduler,
				new ClassDEECoObjectProvider(km, components, ensembles));
		launcher.launch();
	}
}
