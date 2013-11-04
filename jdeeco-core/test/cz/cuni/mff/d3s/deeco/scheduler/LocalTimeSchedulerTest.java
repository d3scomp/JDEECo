/**
 */
package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.executor.Executor;

public class LocalTimeSchedulerTest extends SchedulerTest {

	@Override
	protected Scheduler setUpTested(Executor executor) {
		return new LocalTimeScheduler(executor);
	}
}