/**
 * 
 */
package cz.cuni.mff.d3s.deeco.publish;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeDataProvider;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeDataSender;
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

	private final KnowledgeDataSender knowledgeDataSender;
	private final PeriodicTrigger trigger;
	private final KnowledgeDataProvider knowledgeDataProvider;
	
	public PublisherTask(Scheduler scheduler, KnowledgeDataSender knowledgeDataSender, KnowledgeDataProvider knowledgeDataProvider, PeriodicTrigger trigger) {
		super(scheduler);
		this.knowledgeDataSender = knowledgeDataSender;
		this.trigger = trigger;
		this.knowledgeDataProvider = knowledgeDataProvider;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
	 */
	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {
		//TODO
		knowledgeDataSender.broadcastKnowledgeData(knowledgeDataProvider.getKnowledgeData());
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
		return trigger;
	}

}
