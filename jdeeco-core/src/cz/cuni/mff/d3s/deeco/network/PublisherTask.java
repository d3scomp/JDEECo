/**
 * 
 */
package cz.cuni.mff.d3s.deeco.network;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.PeriodicTriggerExt;
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
	
	/**
	 * Randomized periodic trigger for avoiding recurring collisions of
	 * wireless network publishing.
	 * 
	 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
	 * 
	 */
	private static class PublisherPeriod extends PeriodicTriggerExt {
		public static final double PERIOD_VARIABILITY = 0.2;
		Random random;

		public PublisherPeriod(long period, long seed) {
			super();
			setPeriod(period);
			random = new Random(period);
		}
		
		@Override
		public long getPeriod() {			
			// variability is randomized by at most +- PERIOD_VARIABILITY * getPeriod()
			int variability = (int) (PERIOD_VARIABILITY*super.getPeriod());
			return super.getPeriod() + random.nextInt(2*variability) - variability;
		}
	}
	
	public PublisherTask(Scheduler scheduler, KnowledgeDataPublisher publisher, long period, String host) {
		super(scheduler);		
		long seed = 0;
		for (char c: host.toCharArray())
			seed += c;
		this.trigger = new PublisherPeriod(period, seed);
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
