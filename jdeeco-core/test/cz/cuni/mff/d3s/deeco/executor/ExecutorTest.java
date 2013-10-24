package cz.cuni.mff.d3s.deeco.executor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeReference;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.task.Task;

public class ExecutorTest {

	@Mock
	private Task task;

	private SingleThreadedExecutor singleThreadedExecutorUnderTest;

	private Executor singleThreadedExecutor = new SingleThreadedExecutor();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.singleThreadedExecutorUnderTest = new SingleThreadedExecutor();
	}

	@Test
	public void testSingleThreadedExecutorExecute() {

		ValueSet emptyValueSet = new ValueSet();

		// Specify behavior for mock objects
		when(task.getInputReferences()).thenReturn(
				new LinkedList<KnowledgeReference>());
		when(task.invoke(emptyValueSet)).thenReturn(new ChangeSet());

		singleThreadedExecutorUnderTest.execute(task);

		verify(task).getInputReferences();
		verify(task).invoke(emptyValueSet);
		verifyNoMoreInteractions(task);
	}
}
