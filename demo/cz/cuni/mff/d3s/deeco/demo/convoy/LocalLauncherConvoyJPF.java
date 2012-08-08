package cz.cuni.mff.d3s.deeco.demo.convoy;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.processor.ClassProcessor;
import cz.cuni.mff.d3s.deeco.runtime.JPFLauncher;
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
		try {
			Method processor = ClassProcessor.class.getMethod("main", new Class[]{String[].class});
			processor.invoke(null, (Object []) args);
		} catch (Exception e) {
			System.out.println("LocalLauncherConvoyJPF exception");
		}
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler(km);
		JPFLauncher launcher = new JPFLauncher(km, scheduler);
		launcher.launch(); 
	}
}
