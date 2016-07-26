package cz.cuni.mff.d3s.deeco.scheduler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;

/**
 * TODO: revisit tests - they have become obsolete as we cannot start() or stop() a scheduler any more
 * 
 * Scheduler test suite
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * @author Andranik Muradyan <muradian@d3s.mff.cuni.cz>
 *
 * */
public abstract class SchedulerTest  {

	protected Scheduler tested;
	private Executor executor;
	private TaskTriggerListener testListener;
	
	protected abstract Scheduler setUpTested(Executor executor) throws NoExecutorAvailableException;

	
	@Before
	public void setUp() throws Exception{	
		executor = mock(Executor.class);
		tested = setUpTested(executor);
		testListener = mock(TaskTriggerListener.class);
	}
	
//	@After
//	public void tearDown() throws Exception{	
//		if (tested!=null)
//			tested.stop();
//	}
	
//	@Test 
//	public void testRepeatedExecution() throws InterruptedException{
//		final Task t = mock(Task.class);
//		TimeTrigger p = mock(TimeTrigger.class);
//		// Stubbing mocks.
//		// The period is chosen to be 300 millis because the SingleThreadedScheduler 
//		// may not able to spawn a thread and execute it properly in a shorter 
//		// period of time causing a test failure 
//		when(p.getPeriod()).thenReturn(300L);
//		when(p.getOffset()).thenReturn(0L);
//		when(t.getTimeTrigger()).thenReturn(p);
//		doAnswer(new Answer<Object>() {
//			    public Object answer(InvocationOnMock invocation) {
//			        Object[] args = invocation.getArguments();
//			        tested.executionCompleted((Task)args[0], (Trigger)args[1]);
//			        return null;
//			    }})
//			.when(executor).execute(t, p);
//
//		
//		// WHEN a periodic task is added to a stopped scheduler
//		tested.addTask(t);
//		// THEN it is added to the task list but not started
//		verify(executor, timeout(20).never()).execute(t, p);
//		
//		// WHEN the scheduler is started, it runs for  iterations of the periodic task period
//		tested.getSchedulerNotifier().setTerminationTime(150);
//		tested.start();
//		
//		// Since the period is quite big(300 ms) we will test the 
//		// periodic scheduling only for 5 iterations to save time
//		for( int i = 0; i < 5; i++ ){
//			tested.getSchedulerNotifier().setTerminationTime((i+1)*300+150);
//			tested.start();
//			verify(executor, times(i + 2)).execute(t, p);
//		}
//	}
	
//	@Test
//	public void testPeriodicTaskScheduledWhenSchedulerStarted() throws InterruptedException {
//		Task t = mock(Task.class);
//		TimeTrigger p = mock(TimeTrigger.class);
//		when(p.getPeriod()).thenReturn(11L);
//		when(p.getOffset()).thenReturn(0L);
//		when(t.getTimeTrigger()).thenReturn(p);
//		
//		// WHEN a periodic task is added to a new (stopped) scheduler
//		tested.addTask(t);		
//		// THEN the task is not scheduled				
//		verify(executor, timeout(10).never()).execute(t, p);
//		
//		// WHEN the scheduler is started, runs for a while (longer than the
//		// period) and then stopped
//		tested.getSchedulerNotifier().setTerminationTime(10);
//		tested.start();
//		// THEN the task gets eventually scheduled
//		verify(executor, atLeastOnce()).execute(t, p);
//		
//		reset(executor);
//		
//		// WHEN the running scheduler is stopped for a longer time than the execution period
//		// THEN the task is no longer scheduled		
//		verify(executor, timeout(10).never()).execute(t, p);		
//		
//	}
	
//	@Test
//	public void testTriggeredTaskScheduledOnlyWhenTriggered() throws InterruptedException {
//		Task t = createTriggeredTask();
//		Trigger tr = mock(Trigger.class); 
//		
//		// WHEN a triggered task is added to a stopped scheduler and the trigger is triggered
//		tested.addTask(t);
//		testListener.triggered(t, tr);
//		// THEN the process in not scheduled		
//		verify(executor, never()).execute(t, tr);		
//		
//		// WHEN the scheduler is started with a registered triggered task
//		tested.getSchedulerNotifier().setTerminationTime(100);
//		tested.start();
//		// THEN it is not scheduled if no trigger is triggered			
//		verify(executor, never()).execute(t, tr);
//		
//		// WHEN the corresponding trigger is triggered
//		// TODO find a way to test triggered processes
////		testListener.triggered(t, tr);
//		// THEN the process is scheduled (exactly once) 
//		// (we use a small timeout because the scheduler might have a separate thread for scheduling)
////		verify(executor, timeout(20).times(1)).execute(t, tr);
//		
//				
//		// WHEN the scheduler is stopped and the trigger is triggered
//		tested.stop();
//		reset(executor);		
//		testListener.triggered(t, tr);
//		// THEN the process in not scheduled anymore
//		verify(executor, never()).execute(t, tr);		
//	
//	}
	
//	@Test
//	public void testResetingSimulationQueue() {
//		// WHEN the scheduler is started with a registered triggered task
//		tested.getSchedulerNotifier().setTerminationTime(100);
//		tested.start();
//		assertEquals(tested.getSchedulerNotifier().getCurrentMilliseconds(),100);
//		
//		tested.getSchedulerNotifier().reset();
//		assertEquals(tested.getSchedulerNotifier().getCurrentMilliseconds(),0);
//	}
	
	/**
	 * Creates a purely triggered task which stores the given trigger listener
	 * into {@link #testListener}.
	 */
	private Task createTriggeredTask() {
		
		Task t = new Task(tested, "triggered_test_task") {	
			@Override
			public TimeTrigger getTimeTrigger() {
				return null;
			}
			
			@Override
			protected void unregisterTriggers() {}			
			@Override
			protected void registerTriggers() {}			
			@Override
			public void invoke(Trigger trigger) {}
			
			@Override
			public void setTriggerListener(TaskTriggerListener listener) {				
				super.setTriggerListener(listener);
				testListener = listener;
			}
			
		};
		
		return t;
	}
}
