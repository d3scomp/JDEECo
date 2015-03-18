package cz.cuni.mff.d3s.deeco.scheduler;

import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;

/**
 * TODO 
 */
public class SingleThreadedSchedulerTest extends SchedulerTest {

	@Override
	protected Scheduler setUpTested(Executor executor) throws NoExecutorAvailableException {
		DiscreteEventTimer simulation = new DiscreteEventTimer();
		return new SingleThreadedScheduler(executor, simulation, Mockito.mock(DEECoNode.class));
	}
	

	
}