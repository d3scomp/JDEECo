package cz.cuni.mff.d3s.deeco.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagersView;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowsTriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.model.runtime.SampleRuntimeModel;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public class EnsembleTaskTest {
	
	private SampleRuntimeModel model;
	
	@Mock
	private KnowledgeManager knowledgeManager;
	@Mock
	private KnowledgeManagersView shadowReplicasAccess;
	@Mock
	private ReadOnlyKnowledgeManager shadowKnowledgeManager;
	@Mock
	private TaskTriggerListener taskTriggerListener;
	@Captor
	private ArgumentCaptor<TriggerListener> knowledgeManagerTriggerListenerCaptor;
	@Captor
	private ArgumentCaptor<ShadowsTriggerListener> shadowReplicasTriggerListenerCaptor;
	@Mock
	private Scheduler scheduler;
	
	private Task task;
	
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		
		model = new SampleRuntimeModel();

		doNothing().when(knowledgeManager).register(eq(model.ensembleKnowledgeChangeTrigger), knowledgeManagerTriggerListenerCaptor.capture());
		doNothing().when(shadowReplicasAccess).register(eq(model.ensembleKnowledgeChangeTrigger), shadowReplicasTriggerListenerCaptor.capture());
		
		model.setKnowledgeManager(knowledgeManager);
		model.setOtherKnowledgeManagersAccess(shadowReplicasAccess);

		this.task = new EnsembleTask(model.ensembleController, scheduler);
	}
	
	@Test
	public void testSchedulingPeriod() {
		// GIVEN an EnsembleTask initialized with an InstanceEnsemblingController
		// WHEN getSchedulingPeriod is called
		long period = task.getPeriodicTrigger().getPeriod();
		// THEN it returns the period specified in the model
		// THEN it returns the period specified in the model
		assertEquals(model.ensemblePeriodicTrigger.getPeriod(), period);

	}
	
	@Test
	public void testTrigger() {
		// GIVEN an EnsembleTask initialized with an InstanceEnsemblingController
		// WHEN a trigger listener (i.e. scheduler) is registered at the task
		task.setTriggerListener(taskTriggerListener);
		// THEN the task registers a trigger listener (regardless whether it is a trigger on coordinator's or member's knowledge) on the knowledge manager
		verify(knowledgeManager).register(eq(model.ensembleKnowledgeChangeTrigger), any(TriggerListener.class)); // FIXME TB: This is wrong, because the task is supposed to register a trigger without its root (member/coord). The same applies below.
		// AND the task register a trigger listener (regardless whether it is a trigger on coordinator's or member's knowledge) on the shadow replicas
		verify(shadowReplicasAccess).register(eq(model.ensembleKnowledgeChangeTrigger), any(ShadowsTriggerListener.class));		

		// WHEN a trigger comes from the knowledge manager
		knowledgeManagerTriggerListenerCaptor.getValue().triggered(model.ensembleKnowledgeChangeTrigger);
		// THEN the task calls the registered listener
		verify(taskTriggerListener).triggered(task, model.ensembleKnowledgeChangeTrigger);
				
		// WHEN a trigger comes from the shadow replica
		// TODO, it would make more sense, if the trigger notification from the shadow replicas would carry a reference to the particular read-only knowledge
		// manager
		reset(taskTriggerListener); // Without this, we would have to say that the verify below verifies two invocations -- because one already occurred above.
		shadowReplicasTriggerListenerCaptor.getValue().triggered(shadowKnowledgeManager, model.ensembleKnowledgeChangeTrigger);
		// THEN the task calls the registered listener
		verify(taskTriggerListener).triggered(task, model.ensembleKnowledgeChangeTrigger);
		
		// WHEN the listener (i.e. the scheduler) is unregistered
		task.unsetTriggerListener();
		// THEN the trigger is unregistered at the knowledge manager
		verify(knowledgeManager).unregister(model.ensembleKnowledgeChangeTrigger, knowledgeManagerTriggerListenerCaptor.getValue());
		// AND the trigger is unregistered at the shadow replicas
		verify(knowledgeManager).unregister(model.ensembleKnowledgeChangeTrigger, knowledgeManagerTriggerListenerCaptor.getValue());

		// WHEN a spurious trigger comes from the knowledge manager
		// TODO
		// THEN it is not propagated to the listener (i.e. the scheduler)
		verifyNoMoreInteractions(taskTriggerListener);

		// WHEN a spurious trigger comes from a shadow replica
		// TODO
		// THEN it is not propagated to the listener (i.e. the scheduler)
		verifyNoMoreInteractions(taskTriggerListener);
	}
	
	@Test
	@Ignore
	public void testEnsembleTaskInvoke() {
		// GIVEN an EnsembleTask initialized with an InstanceEnsemblingController
		// WHEN invoke on the task is called
		// THEN it gets the needed local knowledge
		// AND it retrieves the other knowledge managers
		// AND it gets knowledge from them
		// AND it executes the membership
		// AND it executes knowledge exchange for those for which the membership was satisfied 
		// AND it updates the instance knowledge with outputs of the knowledge exchange
		
		fail("Not implemented!");
	}	
}
