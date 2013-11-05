/**
 */
package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.executor.Executor;

<<<<<<< HEAD
public class LocalTimeSchedulerTest extends SchedulerTest {

	@Override
	protected Scheduler setUpTested(Executor executor) {
		return new LocalTimeScheduler(executor);
=======
//FIXME TB: The class is missing a header which states the author

public class LocalTimeSchedulerTest {
	LocalTimeScheduler sched;
	Executor executor;
	
	@Before
	public void setUp() throws Exception{		
		executor = mock(Executor.class);
		sched = new LocalTimeScheduler();
		sched.setExecutor(executor);
>>>>>>> origin/newgen
	}
}