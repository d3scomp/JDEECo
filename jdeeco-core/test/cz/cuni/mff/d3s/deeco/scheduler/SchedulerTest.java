package cz.cuni.mff.d3s.deeco.scheduler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;

/**
 * Scheduler test suite
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * @author Andranik Muradyan <muradian@d3s.mff.cuni.cz>
 *
 * */
public abstract class SchedulerTest  {

	protected Scheduler tested;
	protected Executor executor;
	protected TaskTriggerListener testListener;
	
	protected abstract Scheduler setUpTested(Executor executor2);

	
	@Before
	public void setUp() throws Exception{	
		executor = mock(Executor.class);
		tested = setUpTested(executor);
		testListener = mock(TaskTriggerListener.class);
	}
	
	@After
	public void tearDown() throws Exception{	
		if (tested!=null)
			tested.stop();
	}

	// TODO TB: Shouldn't we have also a test that tests repeated execution? For instnce to start the scheduler, let it run for 2 secs and see that a periodic
	// task with a period of 100 ms got scheduled approx. 200 times?
	
	@Test
	public void testPeriodicTaskScheduledWhenSchedulerStarted() throws InterruptedException {
		Task t = mock(Task.class);
		PeriodicTrigger p = mock(PeriodicTrigger.class);
		when(p.getPeriod()).thenReturn(11L);
		when(t.getPeriodicTrigger()).thenReturn(p);
		
		// WHEN a periodic task is added to a new (stopped) scheduler
		tested.addTask(t);		
		// THEN the task is not scheduled				
		verify(executor, timeout(10).never()).execute(t, p);
		
		// WHEN the scheduler is started, runs for a while (longer than the
		// period) and then stopped
		tested.start();
		Thread.sleep(10);
		tested.stop();
		// THEN the task gets eventually scheduled
		verify(executor, atLeastOnce()).execute(t, p);
		
		reset(executor);
		
		// WHEN the running scheduler is stopped a bit longer (FIXME TB: not sure what it means) 
		// THEN the task is no longer scheduled		
		verify(executor, timeout(10).never()).execute(t, p);		
		
	}
	
	@Test
	public void testPeriodicTaskAutomaticallyScheduledWhenAddedToRunningScheduler() throws InterruptedException {
		Task t = mock(Task.class);
		PeriodicTrigger p = mock(PeriodicTrigger.class);
		when(p.getPeriod()).thenReturn(11L);
		when(t.getPeriodicTrigger()).thenReturn(p);
		tested.start();

		// WHEN a task is added to a running scheduler
		tested.addTask(t);
		// THEN it gets eventually scheduled	
		verify(executor, timeout(10).atLeastOnce()).execute(t, p);
	}
	
	@Test
	public void testPeriodicTaskNotScheduledWhenRemovedRunningScheduler() throws InterruptedException {
		Task t = mock(Task.class);
		PeriodicTrigger p = mock(PeriodicTrigger.class);
		when(p.getPeriod()).thenReturn(11L);
		when(t.getPeriodicTrigger()).thenReturn(p);
		
		tested.addTask(t);
		tested.start();

		// WHEN a task is removed from a running scheduler
		tested.removeTask(t);				
		// THEN it gets eventually un-scheduled
		reset(executor);
		verify(executor, timeout(10).never()).execute(t, p);		
	}
	
	@Test
	public void testTriggeredTaskScheduledOnlyWhenTriggered() throws InterruptedException {
		Task t = createTriggeredTask();
		Trigger tr = mock(Trigger.class); 
		
		// WHEN a triggered task is added to a stopped scheduler and the trigger is triggered
		tested.addTask(t);
		testListener.triggered(t, tr);
		// THEN the process in not scheduled		
		verify(executor, never()).execute(t, tr);		
		
		// WHEN the scheduler is started with a registered triggered task
		tested.start();
		// THEN it is not scheduled if no trigger is triggered			
		verify(executor, timeout(10).never()).execute(t, tr);
		
		// WHEN the corresponding trigger is triggered
		testListener.triggered(t, tr);
		// THEN the process is scheduled (exactly once)
		verify(executor, times(1)).execute(t, tr);
		
		// WHEN the scheduler is stopped and the trigger is triggered
		reset(executor);
		tested.stop();
		testListener.triggered(t, tr);
		// THEN the process in not scheduled anymore
		verify(executor, never()).execute(t, tr);		
		
		// WHEN the task is removed from a running scheduler and the trigger is triggered
		tested.start();
		tested.removeTask(t);		
		testListener.triggered(t, tr);
		// THEN the process in not scheduled		
		verify(executor, never()).execute(t, tr);		
	}
	
	@Test
	public void testTriggerListenerRegisteredAfterAddWhenRunning() {
		Task t = mock(Task.class);
		
		tested.start();
		
		// WHEN a task is added to a running scheduler
		tested.addTask(t);
		// THEN the scheduler registers a trigger listener for the task
		verify(t, times(1)).setTriggerListener(any(TaskTriggerListener.class));
		
		// WHEN repeating the action
		reset(t);
		tested.addTask(t);
		// THEN nothing happens anymore
		verify(t, never()).setTriggerListener(any(TaskTriggerListener.class));
	}
	
	@Test
	public void testTriggerListenerUnregisteredAfterRemoveWhenRunning() {
		Task t = mock(Task.class);
		tested.start();
		tested.addTask(t);
		
		// WHEN a task is removed from a running scheduler		
		tested.removeTask(t);
		
		// THEN the scheduler unregisters its trigger listener for the task
		verify(t, times(1)).setTriggerListener(null);
		
		// WHEN repeating the action
		reset(t);
		tested.removeTask(t);
		// THEN nothing happens anymore
		verify(t, never()).setTriggerListener(null);
	}
	
	@Test
	public void testTriggerListenerRegisteredAfterStartWhenAdded() {
		Task t = mock(Task.class);
		tested.addTask(t);
		
		// WHEN a scheduler with a single added task is started
		tested.start();
		// THEN the scheduler registers a trigger listener for the task
		verify(t, times(1)).setTriggerListener(any(TaskTriggerListener.class));
		
		// WHEN repeating the action
		reset(t);
		tested.start();
		// THEN nothing happens anymore
		verify(t, never()).setTriggerListener(any(TaskTriggerListener.class));
	}
	
	@Test
	public void testTriggerListenerUnregisteredAfterStopWhenAdded() {
		Task t = mock(Task.class);
		tested.start();
		tested.addTask(t);
		
		// WHEN a scheduler with a single added task is stopped
		tested.stop();
		
		// THEN the scheduler unregisters its trigger listener for the task
		verify(t, times(1)).setTriggerListener(null);
		
		// WHEN repeating the action
		reset(t);
		tested.stop();
		// THEN nothing happens anymore
		verify(t, never()).setTriggerListener(null);
	}
	
	/**
	 * Creates a purely triggered task which stores the given trigger listener
	 * into {@link #testListener}.
	 */
	private Task createTriggeredTask() {
		
		Task t = new Task(tested) {	
			@Override
			public PeriodicTrigger getPeriodicTrigger() {
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
