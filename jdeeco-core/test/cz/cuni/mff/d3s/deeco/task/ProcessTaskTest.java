package cz.cuni.mff.d3s.deeco.task;

import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.model.runtime.api.InstanceProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Process;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public class ProcessTaskTest {
	
	@Mock
	private InstanceProcess instanceProcess;
	@Mock
	private Process process;
	@Mock
	private SchedulingSpecification schedule;
	
	private Task processTask;
	
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(instanceProcess.getProcess()).thenReturn(process);
		when(process.getSchedule()).thenReturn(schedule);
		when(schedule.getPeriod()).thenReturn(42L);

		this.processTask = new ProcessTask(instanceProcess);
	}
	
	@Test
	public void testSchedulingPeriod() {
		// GIVEN a ProcessTask initialized with an InstanceProcess
		// WHEN getSchedulingPeriod is called
		long period = processTask.getSchedulingPeriod();
		// THEN it returns the period specified in the model
		assertEquals(42L, period);
	}
	
	@Test
	@Ignore
	public void testTrigger() {
		// GIVEN a ProcessTask initialized with an InstanceProcess
		// WHEN a trigger listener is registered
		// AND a trigger comes from the knowledge manager
		// THEN the task calls the registered listener
		
		// WHEN the listener is unregistered
		// AND a trigger comes from the knowledge manager
		// THEN the task does not call the registered listener
	}
	
	@Test
	@Ignore
	public void testProcessTaskInvoke() {
		// GIVEN a ProcessTask initialized with an InstanceProcess
		// WHEN invoke on the task is called
		// THEN it gets the knowledge needed for execution of the task
		// AND it executes the process method
		// AND it updates knowledge with outputs of the process method

		fail("Not implemented!");
	}	
}
