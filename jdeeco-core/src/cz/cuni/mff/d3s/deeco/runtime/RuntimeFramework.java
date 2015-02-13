package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.integrity.RatingsManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * @TODO(IG) : Check if it's OK to remove this interface (the its tests) altogether
 * 
 * JDEECo runtime framework management interface.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public interface RuntimeFramework extends DEECoPlugin {

	Scheduler getScheduler();
	KnowledgeManagerContainer getContainer();
	RatingsManager getRatingsManager();
}
