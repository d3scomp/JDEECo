/**
 * Scheduler test suite
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * @author Andranik Muradyan <muradian@d3s.mff.cuni.cz>
 *
 * */


package cz.cuni.mff.d3s.deeco.scheduler;

import java.util.Arrays;

import org.eclipse.emf.common.util.BasicEList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;
import static org.mockito.Mockito.*;

public abstract class SchedulerTest  {

	protected Scheduler tested;
	protected Executor executor;
	protected TriggerListener testListener;
	
	protected abstract Scheduler setUpTested(Executor executor2);

	
	@Before
	public void setUp() throws Exception{	
		executor = mock(Executor.class);
		tested = setUpTested(executor);
		testListener = mock(TriggerListener.class);
	}
	
	@After
	public void tearDown() throws Exception{	
		if (tested!=null)
			tested.stop();
	}

	@Test
	public void testPeriodicTaskScheduledWhenSchedulerStarted() throws InterruptedException {
		Task t = mock(Task.class);
		when(t.getSchedulingPeriod()).thenReturn(1l);
		
		// WHEN a periodic task is added to a new (stopped) scheduler
		tested.addTask(t);		
		// THEN the task is not scheduled				
		verify(executor, timeout(10).never()).execute(t);
		
		// WHEN the scheduler is started, runs for a while (longer than the
		// period) and then stopped
		tested.start();
		Thread.sleep(10);
		tested.stop();
		// THEN the task gets eventually scheduled
		verify(executor, atLeastOnce()).execute(t);
		
		reset(executor);
		
		// WHEN the running scheduler is stopped a bit longer
		// THEN the task is no longer scheduled		
		verify(executor, timeout(10).never()).execute(t);		
		
	}
	
	@Test
	public void testPeriodicTaskAutomaticallyScheduledWhenAddedToRunningScheduler() throws InterruptedException {
		Task t = mock(Task.class);
		when(t.getSchedulingPeriod()).thenReturn(1l);
		tested.start();

		// WHEN a task is added to a running scheduler
		tested.addTask(t);
		// THEN it gets eventually scheduled	
		verify(executor, timeout(10000).atLeastOnce()).execute(t);
	}
	
	@Test
	public void testPeriodicTaskNotScheduledWhenRemovedRunningScheduler() throws InterruptedException {
		Task t = mock(Task.class);
		when(t.getSchedulingPeriod()).thenReturn(1l);		
		
		tested.addTask(t);
		tested.start();

		// WHEN a task is removed from a running scheduler
		tested.removeTask(t);				
		// THEN it gets eventually un-scheduled
		reset(executor);
		verify(executor, timeout(10).never()).execute(t);		
	}
	
	@Test
	public void testTriggeredTaskScheduledOnlyWhenTriggeredAndSchedulerRunning() throws InterruptedException {
		Trigger trigger = mock(Trigger.class);
		Task t = createTriggeredTask(trigger);
		
		// WHEN a triggered task is added to a stopped scheduler and the trigger is triggered
		tested.addTask(t);
		testListener.triggered(trigger);
		// THEN the process in not scheduled		
		verify(executor, never()).execute(t);		
		
		// WHEN the scheduler is started with a registered triggered task
		tested.start();
		// THEN it is not scheduled if no trigger is triggered			
		verify(executor, timeout(10).never()).execute(t);
		
		// WHEN the corresponding trigger is triggered
		testListener.triggered(trigger);
		// THEN the process is scheduled (exactly once)
		verify(executor, times(1)).execute(t);
	}
	
	@Test
	public void testTriggeredTaskScheduledWhenSchedulerRunningOrInTaskList() throws InterruptedException{
		Trigger trigger = mock(Trigger.class);
		Task t = createTriggeredTask(trigger);
		
		// WHEN the scheduler is stopped and the trigger is triggered
		tested.stop();
		testListener.triggered(trigger);
		// THEN the process in not scheduled anymore
		verify(executor, never()).execute(t);
		
		// WHEN the task is removed from a running scheduler and the trigger is triggered
		tested.start();
		tested.removeTask(t);		
		testListener.triggered(trigger);
		// THEN the process in not scheduled		
		verify(executor, never()).execute(t);
	}
	
	@Test
	public void testTriggerListenerRegisteredAfterAddWhenRunning() {
		Task t = mock(Task.class);
		
		tested.start();
		
		// WHEN a task is added to a running scheduler
		tested.addTask(t);
		// THEN the scheduler registers a trigger listener for the task
		verify(t, times(1)).setTriggerListener(any(TriggerListener.class));

		// WHEN repeating the action
		reset(t);
		tested.addTask(t);
		// THEN nothing happens anymore
		verify(t, never()).setTriggerListener(any(TriggerListener.class));
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
		verify(t, times(1)).setTriggerListener(any(TriggerListener.class));
		
		// WHEN repeating the action
		reset(t);
		tested.start();
		// THEN nothing happens anymore
		verify(t, never()).setTriggerListener(any(TriggerListener.class));
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
	private Task createTriggeredTask(Trigger trigger) {
		
		SchedulingSpecification sspec = mock(SchedulingSpecification.class);
		when(sspec.getPeriod()).thenReturn(0l);
		when(sspec.getTriggers()).thenReturn(new BasicEList<>(Arrays.asList(trigger)));
		
		Task t = new Task(sspec, tested) {		
			
			@Override
			protected void unregisterTriggers() {}			
			@Override
			protected void registerTriggers() {}			
			@Override
			public void invoke() {}
			
			@Override
			public void setTriggerListener(TriggerListener listener) {				
				super.setTriggerListener(listener);
				testListener = listener;
			}
			
		};
		
		return t;
	}
}
