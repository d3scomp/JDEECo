package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.executor.Executor;

/**
 * TODO 
 */
public class SingleThreadedSchedulerTest extends SchedulerTest {

	@Override
	protected Scheduler setUpTested(Executor executor) throws NoExecutorAvailableException {
		DiscreteEventSimulation simulation = new DiscreteEventSimulation();
		return new SingleThreadedScheduler(executor, simulation);
	}
	

	
}