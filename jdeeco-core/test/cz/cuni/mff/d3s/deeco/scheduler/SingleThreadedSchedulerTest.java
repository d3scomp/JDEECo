package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.executor.Executor;

/**
 * 
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class SingleThreadedSchedulerTest extends SchedulerTest {

	@Override
	protected Scheduler setUpTested(Executor executor) {
		Scheduler s = new SingleThreadedScheduler();
		s.setExecutor(executor);
		return s;
	}
}