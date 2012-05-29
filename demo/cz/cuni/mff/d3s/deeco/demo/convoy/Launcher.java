package cz.cuni.mff.d3s.deeco.demo.convoy;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.jini.TSKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

/**
 * Main class for launching the application.
 * 
 * @author Michal Kit
 * 
 */
public class Launcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Class[] classes = { RobotLeaderComponent.class };
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
//				new TSKnowledgeRepository());
		Runtime runtime = new Runtime(classes, null, km);
	}

}
