/**
 * 
 */
package cz.cuni.mff.d3s.deeco.network;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
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
	public static final int DEFAULT_PUBLISHING_PERIOD = 1000;

	private final KnowledgeDataPublisher publisher;
	private final TimeTrigger trigger;
	
	public PublisherTask(Scheduler scheduler, KnowledgeDataPublisher publisher, TimeTrigger trigger, String host) {
		super(scheduler);		

		this.trigger = trigger;
		this.publisher = publisher;
		
		Log.d(String.format("PublisherTask at %s uses publishing period %d", host, trigger.getPeriod()));
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
	public TimeTrigger getTimeTrigger() {
		return trigger;
	}
	
	public String toString() {
		return "PublisherTask";
	}

}
