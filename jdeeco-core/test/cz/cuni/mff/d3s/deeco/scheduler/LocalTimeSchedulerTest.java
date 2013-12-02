package cz.cuni.mff.d3s.deeco.scheduler;

import static org.mockito.Mockito.mock;

import org.junit.Before;

import cz.cuni.mff.d3s.deeco.executor.Executor;

/**
 * 
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class LocalTimeSchedulerTest extends SchedulerTest {

	private Executor executor;
	
	@Before
	public void setUp() throws Exception{	
		executor = mock(Executor.class);
		tested = new LocalTimeScheduler();
		tested = setUpTested(executor);
		super.setUp();
	}
		
	@Override
	protected LocalTimeScheduler setUpTested(Executor executor) {
		LocalTimeScheduler s = new LocalTimeScheduler();
		s.setExecutor(executor);
		return s;
	}	
}