package cz.cuni.mff.d3s.deeco.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

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
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
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
	private ReadOnlyKnowledgeManager shadowKnowledgeManager1;
	@Mock
	private ReadOnlyKnowledgeManager shadowKnowledgeManager2;
	@Mock
	private TaskTriggerListener taskTriggerListener;
	@Captor
	private ArgumentCaptor<TriggerListener> knowledgeManagerTriggerListenerCaptor;
	@Captor
	private ArgumentCaptor<ShadowsTriggerListener> shadowReplicasTriggerListenerCaptor;
	@Captor
	private ArgumentCaptor<Trigger> triggerCaptor;
	@Mock
	private Scheduler scheduler;
	
	private Task task;
	
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		
		model = new SampleRuntimeModel();
		
		doNothing().when(knowledgeManager).register(any(Trigger.class), knowledgeManagerTriggerListenerCaptor.capture());
		doNothing().when(shadowReplicasAccess).register(any(Trigger.class), shadowReplicasTriggerListenerCaptor.capture());

		Collection<ReadOnlyKnowledgeManager> shadowKnowledgeManagersCollection = new LinkedList<ReadOnlyKnowledgeManager>();
		shadowKnowledgeManagersCollection.add(shadowKnowledgeManager1);
		shadowKnowledgeManagersCollection.add(shadowKnowledgeManager2);
		when(shadowReplicasAccess.getOthersKnowledgeManagers()).thenReturn(shadowKnowledgeManagersCollection);
		
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

	private boolean equalToStrippedPath(KnowledgePath fullPath, KnowledgePath strippedPath) {
		if (fullPath.getNodes().size() != strippedPath.getNodes().size() + 1) {
			return false;
		}

		Iterator<PathNode> fullPathIter = fullPath.getNodes().iterator();
		Iterator<PathNode> strippedPathIter = strippedPath.getNodes().iterator();
		
		fullPathIter.next();
		
		while (fullPathIter.hasNext()) {
			if (!fullPathIter.next().equals(strippedPathIter.next())) {
				return false;
			}
		}
		
		return true;
	}
	
	@Test
	public void testTrigger() {
		// GIVEN an EnsembleTask initialized with an InstanceEnsemblingController
		// WHEN a trigger listener (i.e. scheduler) is registered at the task
		task.setTriggerListener(taskTriggerListener);
		// THEN the task registers a trigger listener (regardless whether it is a trigger on coordinator's or member's knowledge) on the knowledge manager
		verify(knowledgeManager).register(triggerCaptor.capture(), any(TriggerListener.class));
		// AND the trigger has a knowledge path which omits the member/coordinator prefix
		assert(equalToStrippedPath(model.ensembleKnowledgeChangeTrigger.getKnowledgePath(), ((KnowledgeChangeTrigger)triggerCaptor.getValue()).getKnowledgePath()));
		// AND the task registers a trigger listener (regardless whether it is a trigger on coordinator's or member's knowledge) on the shadow replicas
		verify(shadowReplicasAccess).register(eq(triggerCaptor.getValue()), any(ShadowsTriggerListener.class));		

		// WHEN a trigger comes from the knowledge manager
		knowledgeManagerTriggerListenerCaptor.getValue().triggered(triggerCaptor.getValue());
		// THEN the task calls the registered listener
		verify(taskTriggerListener).triggered(eq(task), any(Trigger.class));
				
		// WHEN a trigger comes from the shadow replica
		reset(taskTriggerListener); // Without this, we would have to say that the verify below verifies two invocations -- because one already occurred above.
		shadowReplicasTriggerListenerCaptor.getValue().triggered(shadowKnowledgeManager1, triggerCaptor.getValue());
		// THEN the task calls the registered listener
		verify(taskTriggerListener).triggered(eq(task), any(Trigger.class));
		
		// WHEN the listener (i.e. the scheduler) is unregistered
		task.unsetTriggerListener();
		// THEN the trigger is unregistered at the knowledge manager
		verify(knowledgeManager).unregister(triggerCaptor.getValue(), knowledgeManagerTriggerListenerCaptor.getValue());
		// AND the trigger is unregistered at the shadow replicas
		verify(knowledgeManager).unregister(triggerCaptor.getValue(), knowledgeManagerTriggerListenerCaptor.getValue());

		// WHEN a spurious trigger comes from the knowledge manager
		knowledgeManagerTriggerListenerCaptor.getValue().triggered(triggerCaptor.getValue());
		// THEN it is not propagated to the listener (i.e. the scheduler)
		verifyNoMoreInteractions(taskTriggerListener);

		// WHEN a spurious trigger comes from a shadow replica
		shadowReplicasTriggerListenerCaptor.getValue().triggered(shadowKnowledgeManager1, triggerCaptor.getValue());
		// THEN it is not propagated to the listener (i.e. the scheduler)
		verifyNoMoreInteractions(taskTriggerListener);
	}
	
	@Test
	@Ignore
	public void testEnsembleTaskInvoke() throws Exception {
		// GIVEN an EnsembleTask initialized with an InstanceEnsemblingController
		model.resetMembershipMethodCallCounter();
		model.resetExchangeMethodCallCounter();
		
		// WHEN invoke (with periodic trigger) is called on the task
		task.invoke(model.ensemblePeriodicTrigger);
		// THEN it gets the needed local knowledge
		verify(knowledgeManager).get(anyCollectionOf(KnowledgePath.class));
		// AND it retrieves the other knowledge managers
		verify(shadowReplicasAccess).getOthersKnowledgeManagers();
		// AND it gets knowledge from them
		verify(shadowKnowledgeManager1).get(anyCollectionOf(KnowledgePath.class));
		verify(shadowKnowledgeManager2).get(anyCollectionOf(KnowledgePath.class));		
		// AND it executes the membership
		assertTrue(model.getMembershipMethodCallCounter() == 2);
		assertTrue(model.getExchangeMethodCallCounter() == 1);
		// AND it executes knowledge exchange for those for which the membership was satisfied 
		// AND it updates the instance knowledge with outputs of the knowledge exchange
		
		fail("Not implemented!");
	}	
}
