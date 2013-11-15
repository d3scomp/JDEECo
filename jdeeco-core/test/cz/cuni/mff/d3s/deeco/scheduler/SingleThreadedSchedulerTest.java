package cz.cuni.mff.d3s.deeco.scheduler;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.Thread.State;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * 
 * @author Andranik Muradyan <muradian@d3s.mff.cuni.cz>
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class SingleThreadedSchedulerTest extends SchedulerTest {
	
	private SingleThreadedScheduler tested;
	private Executor executor;
	
	@Before
	public void setUp() throws Exception{	
		executor = mock(Executor.class);
		tested = new SingleThreadedScheduler();
		tested.setExecutor(executor);
		super.setUp();
	}
	
	
	@Override
	protected Scheduler setUpTested(Executor executor) {
		TaskQueue queue = mock(TaskQueue.class);
		//SingleThreadedScheduler s = new SingleThreadedScheduler(queue);
		Scheduler s = new SingleThreadedScheduler();
		s.setExecutor(executor);		
		return s;
	}

	@Test
	public void testWaitOnEmptyQueue() throws InterruptedException {		
		// WHEN a new scheduler is started 
		tested.start();
		// We wait for some time, long enough to safely assume that 
		// no racing conditions will affect our test flow
		Thread.sleep(100);
		// THEN the thread is supposed to be in a waiting state 
		assertEquals(State.WAITING, tested.thread.getState());
	}
	
	
	@Test
	public void testDieOnEmptyQueueWhenNotNewTasksMayBeScheduled() throws InterruptedException {
		// Locking on tested.queue to modify the thread value
		// This flag set to false means that the scheduler thread
		// is terminated for good and no new tasks will be scheduled
		// WHEN flag triggeres that the queue is empty
		synchronized (tested.queue) {
			tested.thread.newTasksMayBeScheduled = false;
		}
		
		// After we start the scheduler, the 
		tested.start();
		// And wait enough for the thread to spawn and die
		Thread.sleep(100);
		// Assertion that will confirm that the thread is dead
		// THEN the thread is terminated
		assertEquals(State.TERMINATED, tested.thread.getState());
	}
	
	
	@Test
	public void testRemovesCancelledEvents() throws InterruptedException {
		// Creating mocks of the task and the trigger 
		Task t= mock(Task.class);
		PeriodicTrigger trigger = mock(PeriodicTrigger.class);

		// And stubbing their behaviour
		// Task period has to be big. We donr need to run
		// the task too often since we dont need it
		when(trigger.getPeriod()).thenReturn(20000L);  
		when(t.getPeriodicTrigger()).thenReturn(trigger);
		
		tested.addTask(t);
		tested.start();
		Thread.sleep(200);
		
		// Again locking on an object to prevent data manipulation by others
		synchronized (tested.queue) {
			reset(executor);
			
			// Check that the task is in the queue
			assertEquals(1, tested.queue.size());
			// Remove it
			tested.removeTask(t);
			// And wake up the thread waiting for the next period (which is comming in 20sec)
			tested.queue.notify();
		}
		
		// Give JVM some time to respond
		Thread.sleep(100);
		
		// Still preventing unwanted data manipulation
		synchronized (tested.queue) {
			// Check that the task is removed
			assertEquals(0, tested.queue.size());
			// And it was not executed
			verify(executor, never()).execute(t, trigger);
		}		
	}
	
	
}