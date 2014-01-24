/**
 * 
 */
package cz.cuni.mff.d3s.deeco.publisher;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class PublisherTask extends Task {

	private final Publisher publisher;
	private final PublisherKnowledgeSource knowledgeSource;
	
	public PublisherTask(Scheduler scheduler, Publisher publisher, PublisherKnowledgeSource knowledgeSource) {
		super(scheduler);
		this.publisher = publisher;
		this.knowledgeSource = knowledgeSource;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
	 */
	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {
		publisher.publish(knowledgeSource.getOwnerId(), knowledgeSource.getKnowledge());
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#registerTriggers()
	 */
	@Override
	protected void registerTriggers() {
		/**
		 * There are no triggers as it is assumed that publishing occurs purely periodically.
		 */
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#unregisterTriggers()
	 */
	@Override
	protected void unregisterTriggers() {
		/**
		 * There are no triggers as it is assumed that publishing occurs purely periodically.
		 */
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#getPeriodicTrigger()
	 */
	@Override
	public PeriodicTrigger getPeriodicTrigger() {
		return publisher.getPeriodicTrigger();
	}
	
	public PublisherKnowledgeSource getPublisherKnowledgeSource() {
		return knowledgeSource;
	}

}
