package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.integrity.RatingsManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * JDEECo runtime framework management interface.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public interface RuntimeFramework {

	/**
	 * Starts the execution of the runtime framework
	 */
	void start();

	/**
	 * Stops the execution of the runtime framework
	 */
	void stop();
	
	/**
	 * Invokes the runnable according to the execution policy of the runtime and
	 * waits for it to finish.
	 * 
	 * @param r	the runnable to invoke.
	 * @throws InterruptedException if the invocation was interrupted.
	 */
	void invokeAndWait(Runnable r) throws InterruptedException;
	
	Scheduler getScheduler();
	KnowledgeManagerContainer getContainer();
	RatingsManager getRatingsManager();
}
