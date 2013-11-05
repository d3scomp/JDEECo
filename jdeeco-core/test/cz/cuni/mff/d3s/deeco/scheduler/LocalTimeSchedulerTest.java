package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.executor.Executor;

/**
 * Factory for Scheduler implementation tests
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class LocalTimeSchedulerTest extends SchedulerTest {

	@Override
	protected Scheduler setUpTested(Executor executor) {
		return new LocalTimeScheduler(executor);
	}
}