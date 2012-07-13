package cz.cuni.mff.d3s.deeco.demo.cloud;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.jini.TSKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Main class for launching the application.
 * 
 * @author Michal Kit
 * 
 */
public class TupleSpaceLauncherCloud {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Class[] classes = { NodeA.class, NodeB.class };
		Class[] ensembles = { MigrationEnsemble.class };
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new TSKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler(km);
		Runtime runtime = new Runtime(classes, ensembles, km, scheduler);
	}
}
