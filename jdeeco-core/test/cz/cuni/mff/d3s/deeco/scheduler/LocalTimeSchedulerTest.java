/**
 */
package cz.cuni.mff.d3s.deeco.scheduler;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;


import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.task.Task;

public class LocalTimeSchedulerTest {
	LocalTimeScheduler sched;
	Executor executor;
	
	@Before
	public void setUp() throws Exception{		
		executor = mock(Executor.class);
		sched = new LocalTimeScheduler(executor);
		
		//dowhen(executor.execute).then();
	}
	
	
}