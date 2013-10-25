package cz.cuni.mff.d3s.deeco.task;

import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeReference;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;

public class TaskTest {
	
	@Mock
	private SchedulingSpecification schedulingSpecification;
	@Mock
	private ValueSet inputParameters;
	@Mock
	private KnowledgeReference parameterA;
	@Mock
	private KnowledgeReference parameterB;
	
	private Task processTask;
	private Task ensembleTask;
	
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.processTask = new ProcessTask();
		this.ensembleTask = new EnsembleTask();
	}
	
	@Test
	public void testProcessInvoke() {
		Object parameterAValue = new Object();
		Object parameterBValue = new Object();
		
		//Train mock
		when(inputParameters.getValue(parameterA)).thenReturn(parameterAValue);
		when(inputParameters.getValue(parameterB)).thenReturn(parameterBValue);

		processTask.invoke(inputParameters);
		
		verify(inputParameters).getValue(parameterA);
		verify(inputParameters).getValue(parameterB);
		verifyZeroInteractions(inputParameters);
	}
	
	@Test
	public void testProcessGetInputReferences() {
		
	}
	
	@Test
	public void testProcessGetSchedulingSpecification() {
		
	}
	
	@Test
	public void testEnsembleInvoke() {
		Object parameterAValue = new Object();
		Object parameterBValue = new Object();
		
		//Train mock
		when(inputParameters.getValue(parameterA)).thenReturn(parameterAValue);
		when(inputParameters.getValue(parameterB)).thenReturn(parameterBValue);

		ensembleTask.invoke(inputParameters);
		
		verify(inputParameters, atMost(2)).getValue(parameterA);
		verify(inputParameters, atMost(2)).getValue(parameterB);
		verifyZeroInteractions(inputParameters);
	}
	
	@Test
	public void testEnsembleGetInputReferences() {
		
	}
	
	@Test
	public void testEnsembleGetSchedulingSpecification() {
		
	}
}
