/**
 */
package cz.cuni.mff.d3s.deeco.scheduler;

import static org.mockito.Mockito.mock;

import org.junit.Before;

import cz.cuni.mff.d3s.deeco.executor.Executor;

//FIXME: The class is missing a header which states the author

public class LocalTimeSchedulerTest {
	LocalTimeScheduler sched;
	Executor executor;
	
	@Before
	public void setUp() throws Exception{		
		executor = mock(Executor.class);
		sched = new LocalTimeScheduler(executor);
	}
	
	
}