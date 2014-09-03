package cz.cuni.mff.d3s.deeco.executor;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;



import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * Base for testing all Executor implementations.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public abstract class ExecutorTest {
	
	protected Executor tested;
	protected ExecutionListener listener;
	protected Task taskSuccess; 
	protected Task taskFail;
	protected Trigger trigger;
	protected Exception taskException; 
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

		
	protected abstract Executor setUpTested();

	
	@Before
	public void setUp() throws Exception {		
		tested = setUpTested();
		listener = mock(ExecutionListener.class);		
		taskSuccess = mock(Task.class);
		trigger = mock(Trigger.class);
		
		taskException = new RuntimeException("Failed invoke");
		taskFail = mock(Task.class);
		doThrow(taskException).when(taskFail).invoke(any(Trigger.class));
	}
	
	@Test
	public void testNoErrorWhenNoExecutionListenerForSuccessTask() {
		// WHEN there is no execution listener and a task that succeeds is
		// scheduled for execution
		tested.setExecutionListener(null);
		tested.execute(taskSuccess, null);
		// THEN no error occurs
	}
	
	@Test
	public void testNoErrorWhenNoExecutionListenerForFailingTask() {
		// WHEN there is no execution listener and a task that fails is
		// scheduled for execution
		tested.setExecutionListener(null);
		tested.execute(taskFail, null);
		// THEN no error occurs
	}
	
	@Test
	public void testExecutionCompletedCalledOnlyWhenListenerSet() {
		// GIVEN an executor with a registered listener
		tested.setExecutionListener(listener);
		// WHEN a task that succeeds is scheduled for execution		
		tested.execute(taskSuccess, trigger);
		// THEN the listener is notified that the task completed
		verify(listener).executionCompleted(taskSuccess, trigger);
		verify(listener, never()).executionFailed(eq(taskSuccess), any(Trigger.class), any(Exception.class));


		reset(listener);
		
		// WHEN the listener is unset and the task is scheduled for execution
		tested.setExecutionListener(null);
		tested.execute(taskSuccess, trigger);
		// THEN it is no longer notified when execute is called
		verify(listener, never()).executionCompleted(taskSuccess, trigger);
		verify(listener, never()).executionFailed(eq(taskSuccess), any(Trigger.class), any(Exception.class));
	}
	
	@Test
	public void testExecutionFailedCalledOnlyWhenListenerSet() {
		// GIVEN an executor with a registered listener
		tested.setExecutionListener(listener);
		// WHEN a task that fails is scheduled for execution		
		tested.execute(taskFail, trigger);
		// THEN the listener is notified that the task completed
		verify(listener).executionFailed(taskFail, trigger, taskException);
		verify(listener, never()).executionCompleted(eq(taskSuccess), any(Trigger.class));

		reset(listener);
		
		// WHEN the listener is unset and the task is scheduled for execution
		tested.setExecutionListener(null);
		tested.execute(taskFail, trigger);
		// THEN it is no longer notified when execute is called
		verify(listener, never()).executionFailed(taskFail, trigger, taskException);
		verify(listener, never()).executionCompleted(eq(taskSuccess), any(Trigger.class));
	}

	@Test
	public void testExecuteNonNullTask() throws Exception {
		// WHEN a non-null task is scheduled for execution
		tested.execute(taskSuccess, null);
		// THEN the task.invoke() is called
		verify(taskSuccess).invoke(null);
	}
	
	@Test
	public void testExecuteNullTask() {
		// WHEN a null task is scheduled for execution
		tested.execute(null, null);
		// THEN nothing bad happens
	}
}
