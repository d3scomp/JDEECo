/**
 * 
 */
package cz.cuni.mff.d3s.deeco.network;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;

/**
 * Task that periodically triggers publishing of knowledge on the network via
 * the given {@link KnowledgeDataPublisher}.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 */
public class PublisherTask extends Task {

	
	private final KnowledgeDataPublisher publisher;
	private final PeriodicTrigger trigger;
	
	
	public PublisherTask(Scheduler scheduler, PeriodicTrigger trigger, KnowledgeDataPublisher publisher) {
		super(scheduler);		
		this.trigger = trigger;
		this.publisher = publisher;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
	 */
	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {
		publisher.publish();
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
