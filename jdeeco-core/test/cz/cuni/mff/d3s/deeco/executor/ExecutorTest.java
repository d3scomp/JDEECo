package cz.cuni.mff.d3s.deeco.executor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.task.Task;

public class ExecutorTest {

	@Mock
	private Task task;

	private SingleThreadedExecutor singleThreadedExecutorUnderTest;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.singleThreadedExecutorUnderTest = new SingleThreadedExecutor();
	}

	@Test
	@Ignore
	public void testSingleThreadedExecutorExecute() {

		singleThreadedExecutorUnderTest.execute(task);

		verify(task).invoke();
		verifyNoMoreInteractions(task);
	}
}
