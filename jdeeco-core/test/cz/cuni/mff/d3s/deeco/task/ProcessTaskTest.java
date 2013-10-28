package cz.cuni.mff.d3s.deeco.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.model.runtime.api.InstanceProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Process;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

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
	@Mock
	private NotificationsForScheduler schedulingNotificationTarget;
	@Mock
	private KnowledgeManager knowledgeManager;
	@Captor
	private ArgumentCaptor<TriggerListener> taskTriggerListenerCaptor;
	@Captor
	private ArgumentCaptor<Trigger> triggerCaptor;
	
	private Task task;
	
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);

		when(instanceProcess.getProcess()).thenReturn(process);
		when(process.getSchedule()).thenReturn(schedule);
		when(schedule.getPeriod()).thenReturn(42L);
		doNothing().when(knowledgeManager).register(triggerCaptor.capture(), taskTriggerListenerCaptor.capture());

		this.task = new ProcessTask(instanceProcess);
	}
	
	@Test
	public void testSchedulingPeriod() {
		// GIVEN a ProcessTask initialized with an InstanceProcess
		// WHEN getSchedulingPeriod is called
		long period = task.getSchedulingPeriod();
		// THEN it returns the period specified in the model
		assertEquals(42L, period);
	}
	
	@Test
	@Ignore
	public void testTrigger() {
		// GIVEN a ProcessTask initialized with an InstanceProcess
		// WHEN a trigger listener is registered
		task.setSchedulingNotificationTarget(schedulingNotificationTarget);
		// AND a trigger comes from the knowledge manager
		taskTriggerListenerCaptor.getValue().triggered(triggerCaptor.getValue());
		// THEN the task calls the registered listener
		verify(schedulingNotificationTarget).triggered(task);
		verifyNoMoreInteractions(schedulingNotificationTarget);
		
		// WHEN the listener is unregistered
		task.setSchedulingNotificationTarget(null);
		// THEN the trigger is unregistered with the knowledge manager
		verify(knowledgeManager).unregister(triggerCaptor.getValue(), taskTriggerListenerCaptor.getValue());
		
		// TODO: TB@JK: Could you please check whether the above is correct?
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
