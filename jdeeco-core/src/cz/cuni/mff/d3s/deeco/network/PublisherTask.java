/**
 * 
 */
package cz.cuni.mff.d3s.deeco.network;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.TimeTriggerExt;
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
	private final PublisherTrigger trigger;
	
	/**
	 * Randomized periodic trigger for avoiding recurring collisions of
	 * wireless network publishing.
	 * 
	 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
	 * 
	 */
	private static class PublisherTrigger extends TimeTriggerExt {
		public static final double PERIOD_VARIABILITY = 0.2;

		Random random;

		public PublisherTrigger(long period, long seed) {
			super();
			setPeriod(period);
			random = new Random(seed);
			// for experiments, publisher task has a random start offset up to its period
			setOffset(random.nextInt((int) period));
			
		}
		
		@Override
		public long getPeriod() {			
			// variability is randomized by at most +- PERIOD_VARIABILITY * getPeriod()
			int variability = (int) (PERIOD_VARIABILITY*super.getPeriod());
			return super.getPeriod() + random.nextInt(2*variability) - variability;
		}		
	}
	
	public PublisherTask(Scheduler scheduler, KnowledgeDataPublisher publisher, String host) {
		this(scheduler, publisher, Integer.getInteger(DeecoProperties.PUBLISHING_PERIOD, DEFAULT_PUBLISHING_PERIOD), host);
	}
	public PublisherTask(Scheduler scheduler, KnowledgeDataPublisher publisher, long period, String host) {
		super(scheduler);		
		long seed = 0;
		for (char c: host.toCharArray())
			seed += c;
		this.trigger = new PublisherTrigger(period, seed);
		this.publisher = publisher;
		
		Log.i(String.format("PublisherTask at %s uses publishing period %d", host, period));
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

}
