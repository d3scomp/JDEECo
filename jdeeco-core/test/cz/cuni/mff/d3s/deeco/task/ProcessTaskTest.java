package cz.cuni.mff.d3s.deeco.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.model.runtime.SampleRuntimeModel;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public class ProcessTaskTest {

	private SampleRuntimeModel model;
	
	@Mock
	private KnowledgeManager knowledgeManager;
	@Mock
	private NotificationsForScheduler schedulingNotificationTarget;
	@Captor
	private ArgumentCaptor<TriggerListener> taskTriggerListenerCaptor;

	private Task task;
	
	
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);

		model = new SampleRuntimeModel();
	/*
		when(instanceProcess.getProcess()).thenReturn(process);
		when(process.getSchedule()).thenReturn(schedule);
		when(schedule.getPeriod()).thenReturn(42L);
	*/
		
		doNothing().when(knowledgeManager).register(eq(model.trigger), taskTriggerListenerCaptor.capture());
		
		this.task = new ProcessTask(model.instanceProcess);
	}
	
	@Test
	public void testSchedulingPeriod() {
		// GIVEN a ProcessTask initialized with an InstanceProcess
		// WHEN getSchedulingPeriod is called
		long period = task.getSchedulingPeriod();
		// THEN it returns the period specified in the model
		assertEquals(model.schedulingSpecification.getPeriod(), period);
	}
	
	@Test
	@Ignore
	public void testTrigger() {
		InOrder inOrder = inOrder(knowledgeManager);
		
		// GIVEN a ProcessTask initialized with an InstanceProcess
		// WHEN a trigger listener is registered
		task.setSchedulingNotificationTarget(schedulingNotificationTarget);
		// AND a trigger comes from the knowledge manager
		taskTriggerListenerCaptor.getValue().triggered(model.trigger);
		// THEN the task calls the registered listener
		inOrder.verify(schedulingNotificationTarget).triggered(task);
		
		// WHEN the listener is unregistered
		task.setSchedulingNotificationTarget(null);
		// THEN the trigger is unregistered with the knowledge manager
		inOrder.verify(knowledgeManager).unregister(model.trigger, taskTriggerListenerCaptor.getValue());

		verifyNoMoreInteractions(schedulingNotificationTarget);

		// TODO: TB@JK: Could you please check whether the above is correct?
	}
	
	@Test
	@Ignore
	public void testProcessTaskInvoke() {
		// GIVEN a ProcessTask initialized with an InstanceProcess
		// WHEN invoke on the task is called
		task.invoke();
		// THEN it gets the knowledge needed for execution of the task
		// AND it executes the process method
		// AND it updates knowledge with outputs of the process method

		fail("Not implemented!");
	}	
}
