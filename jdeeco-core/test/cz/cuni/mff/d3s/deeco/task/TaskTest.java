package cz.cuni.mff.d3s.deeco.task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;

public class TaskTest {
	
	@Mock
	private SchedulingSpecification schedulingSpecification;
	private Task processTask;
	private Task ensembleTask;
	
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.processTask = new ProcessTask();
		this.ensembleTask = new EnsembleTask();
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testProcessInvoke() {
		
	}
	
	@Test
	public void testProcessGetInputReferences() {
		
	}
	
	@Test
	public void testProcessGetSchedulingSpecification() {
		
	}
	
	@Test
	public void testEnsembleInvoke() {
		
	}
	
	@Test
	public void testEnsembleGetInputReferences() {
		
	}
	
	@Test
	public void testEnsembleGetSchedulingSpecification() {
		
	}
}
