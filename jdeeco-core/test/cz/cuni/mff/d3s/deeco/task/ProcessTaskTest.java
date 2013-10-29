package cz.cuni.mff.d3s.deeco.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.anyCollectionOf;

import java.util.Collection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelTest;
import cz.cuni.mff.d3s.deeco.model.runtime.SampleRuntimeModel;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

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
	
	private SampleRuntimeModel.ProcessParameterType inValue;
	private SampleRuntimeModel.ProcessParameterType inOutValue;

	private SampleRuntimeModel.ProcessParameterType expectedInOutValue;
	private SampleRuntimeModel.ProcessParameterType expectedOutValue;

	@Before
	public void setUp() throws Exception {
		initMocks(this);

		model = new SampleRuntimeModel();
		
		inValue = new SampleRuntimeModel.ProcessParameterType(21);
		inOutValue = new SampleRuntimeModel.ProcessParameterType(108);
		expectedInOutValue = new SampleRuntimeModel.ProcessParameterType(108);
		expectedOutValue = new SampleRuntimeModel.ProcessParameterType(0);		
		SampleRuntimeModel.processMethod(inValue, expectedInOutValue, expectedOutValue);
		
		doNothing().when(knowledgeManager).register(eq(model.trigger), taskTriggerListenerCaptor.capture());
		
		when(knowledgeManager.get(anyCollectionOf(KnowledgePath.class))).then(new Answer<ValueSet>() {
			public ValueSet answer(InvocationOnMock invocation) {
				ValueSet result = new ValueSet();
				
				// Use inValue and inOutValue as the responses of the knowledge manager
				for (KnowledgePath kp : ((Collection<KnowledgePath>)invocation.getArguments()[0])) {
					if (kp.equals(model.paramIn.getKnowledgePath())) {
						result.setValue(kp, inValue);
					} else if (kp.equals(model.paramInOut.getKnowledgePath())) {
						result.setValue(kp, inOutValue);
					}
				}
				
				return result;
			}
		});
		
		model.setKnowledgeManager(knowledgeManager);
		
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
		model.resetProcessMethodCallCounter();
		
		// WHEN invoke on the task is called
		task.invoke();
		
		// THEN it gets the knowledge needed for execution of the task
		verify(knowledgeManager).get(anyCollectionOf(KnowledgePath.class));
		
		// AND it executes the process method once
		assertTrue(model.getProcessMethodCallCounter() == 1);
		
		// AND it updates knowledge with only the outputs of the process method
		ArgumentCaptor<ChangeSet> changeSetCaptor = ArgumentCaptor.forClass(ChangeSet.class);
		verify(knowledgeManager).update(changeSetCaptor.capture());
		ChangeSet cs = changeSetCaptor.getValue();
		assertEquals(cs.getValue(model.paramInOut.getKnowledgePath()), expectedInOutValue.value);
		assertEquals(cs.getValue(model.paramOut.getKnowledgePath()), expectedOutValue);
		assertTrue(cs.getDeletedReferences().isEmpty());
		assertTrue(cs.getUpdatedReferences().size() == 2);
	}	
}
