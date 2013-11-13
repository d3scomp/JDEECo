package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;

/**
 * 
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class LocalTimeSchedulerTest extends SchedulerTest {

	private Scheduler tested;
	private Executor executor;
	private TaskTriggerListener testListener;
	
	@Override
	protected LocalTimeScheduler setUpTested(Executor executor) {
		LocalTimeScheduler s = new LocalTimeScheduler();
		s.setExecutor(executor);
		return s;
	}	
}