package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.scheduler.notifier.DiscreteEventSchedulerNotifier;

/**
 * TODO 
 */
public class SingleThreadedSchedulerTest extends SchedulerTest {

	@Override
	protected Scheduler setUpTested(Executor executor) throws NoExecutorAvailableException {
		DiscreteEventSchedulerNotifier simulation = new DiscreteEventSchedulerNotifier();
		return new SingleThreadedScheduler(executor, simulation);
	}
	

	
}