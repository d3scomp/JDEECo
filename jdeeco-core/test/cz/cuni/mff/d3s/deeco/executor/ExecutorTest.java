package cz.cuni.mff.d3s.deeco.executor;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import cz.cuni.mff.d3s.deeco.task.Task;

//FIXME: The class is missing a header which states the author

public abstract class ExecutorTest {
	
	protected Executor tested;
	protected ExecutionListener listener;
	protected Task taskSuccess; 
	protected Task taskFail;
	protected Exception taskException; 
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

		
	protected abstract Executor setUpTested();

	
	@Before
	public void setUp() throws Exception {		
		tested = setUpTested();
		listener = mock(ExecutionListener.class);		
		taskSuccess = mock(Task.class);
		
		taskException = new RuntimeException("Failed invoke");
		taskFail = mock(Task.class);
		doThrow(taskException).when(taskFail).invoke();
	}
	
	@Test
	public void testNoErrorWhenNoExecutionListenerForSuccessTask() {
		// WHEN there is no execution listener and a task that succeeds is
		// scheduled for execution
		tested.setExecutionListener(null);
		tested.execute(taskSuccess);
		// THEN no error occurs
	}
	
	@Test
	public void testNoErrorWhenNoExecutionListenerForFailingTask() {
		// WHEN there is no execution listener and a task that fails is
		// scheduled for execution
		tested.setExecutionListener(null);
		tested.execute(taskFail);
		// THEN no error occurs
	}
	
	@Test
	public void testExecutionCompletedCalledOnlyWhenListenerSet() {
		// GIVEN an executor with a registered listener
		tested.setExecutionListener(listener);
		// WHEN a task that succeeds is scheduled for execution		
		tested.execute(taskSuccess);
		// THEN the listener is notified that the task completed
		verify(listener).executionCompleted(taskSuccess);
		verify(listener, never()).executionFailed(eq(taskSuccess), any(Exception.class));


		reset(listener);
		
		// WHEN the listener is unset and the task is scheduled for execution
		tested.setExecutionListener(null);
		tested.execute(taskSuccess);
		// THEN it is no longer notified when execute is called
		verify(listener, never()).executionCompleted(taskSuccess);
		verify(listener, never()).executionFailed(eq(taskSuccess), any(Exception.class));
	}
	
	@Test
	public void testExecutionFailedCalledOnlyWhenListenerSet() {
		// GIVEN an executor with a registered listener
		tested.setExecutionListener(listener);
		// WHEN a task that fails is scheduled for execution		
		tested.execute(taskFail);
		// THEN the listener is notified that the task completed
		verify(listener).executionFailed(taskFail, taskException);
		verify(listener, never()).executionCompleted(taskSuccess);

		reset(listener);
		
		// WHEN the listener is unset and the task is scheduled for execution
		tested.setExecutionListener(null);
		tested.execute(taskFail);
		// THEN it is no longer notified when execute is called
		verify(listener, never()).executionFailed(taskFail, taskException);
		verify(listener, never()).executionCompleted(taskSuccess);
	}

	@Test
	public void testExecuteNonNullTask() throws Exception {
		// WHEN a non-null task is scheduled for execution
		tested.execute(taskSuccess);
		// THEN the task.invoke() is called
		verify(taskSuccess).invoke();
	}
	
	@Test
	public void testExecuteNullTask() {
		// WHEN a null task is scheduled for execution
		tested.execute(null);
		// THEN nothing bad happens
	}
}
