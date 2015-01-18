package cz.cuni.mff.d3s.deeco.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cz.cuni.mff.d3s.deeco.integrity.RatingsManager;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowsTriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.SampleRuntimeModel;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.runtime.ArchitectureObserver;
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
	private ShadowKnowledgeManagerRegistry shadowReplicasAccess;
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
	@Mock
	private ArchitectureObserver architectureObserver;
	@Mock
	private KnowledgeManagerContainer kmContainer;
	@Mock
	private RatingsManager ratingsManager;
	private Task task;

	private Integer localInValue;
	private Integer localInOutValue;
	private Integer shadow1InValue;
	private Integer shadow1InOutValue;
	private Integer shadow2InValue;
	private Integer shadow2InOutValue;

	private ParamHolder<Integer> expectedLocalInOutValue;
	private ParamHolder<Integer> expectedLocalOutValue;

	private KnowledgePath getStrippedPath(KnowledgePath path) {
		return KnowledgePathHelper.getStrippedPath(path).knowledgePath;
	}
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		
		model = new SampleRuntimeModel();
		
		localInValue = 15;
		localInOutValue = 4;
		shadow1InValue = 15;
		shadow1InOutValue = -2;
		shadow2InValue = 12;
		shadow2InOutValue = 3;
		expectedLocalInOutValue = new ParamHolder<Integer>(localInOutValue);
		expectedLocalOutValue = new ParamHolder<Integer>(0);		
		ParamHolder<Integer> dummyShadowInOutValue = new ParamHolder<Integer>(localInOutValue);
		ParamHolder<Integer> dummyShadowOutValue = new ParamHolder<Integer>(0);		
		// Note that the exchange will call the method twice - once with (local, shadow) and once with (shadow, local). This is because the ensemble can be established
		// with the local component both as a member and as a coordinator. The exchangeMethod is symetrical, thus this does not cause problems.
		SampleRuntimeModel.exchangeMethod(localInValue, expectedLocalOutValue, expectedLocalInOutValue, shadow1InValue, dummyShadowOutValue, dummyShadowInOutValue);
		
		doNothing().when(knowledgeManager).register(any(Trigger.class), knowledgeManagerTriggerListenerCaptor.capture());
		doNothing().when(shadowReplicasAccess).register(any(Trigger.class), shadowReplicasTriggerListenerCaptor.capture());

		Collection<ReadOnlyKnowledgeManager> shadowKnowledgeManagersCollection = new LinkedList<ReadOnlyKnowledgeManager>();
		shadowKnowledgeManagersCollection.add(shadowKnowledgeManager1);
		shadowKnowledgeManagersCollection.add(shadowKnowledgeManager2);
		when(shadowReplicasAccess.getShadowKnowledgeManagers()).thenReturn(shadowKnowledgeManagersCollection);
		
		when(knowledgeManager.get(anyCollectionOf(KnowledgePath.class))).then(new Answer<ValueSet>() {
			public ValueSet answer(InvocationOnMock invocation) {
				ValueSet result = new ValueSet();
				
				// Use inValue and inOutValue as the responses of the knowledge manager
				for (KnowledgePath kp : ((Collection<KnowledgePath>)invocation.getArguments()[0])) {
					if (getStrippedPath(model.exchangeParamCoordIn.getKnowledgePath()).equals(kp)) {
						result.setValue(kp, localInValue);
					} else if (getStrippedPath(model.exchangeParamCoordInOut.getKnowledgePath()).equals(kp)) {
						result.setValue(kp, localInOutValue);
					}
				}
				
				return result;
			}
		});
		
		when(shadowKnowledgeManager1.get(anyCollectionOf(KnowledgePath.class))).then(new Answer<ValueSet>() {
			public ValueSet answer(InvocationOnMock invocation) {
				ValueSet result = new ValueSet();
				
				// Use inValue and inOutValue as the responses of the knowledge manager
				for (KnowledgePath kp : ((Collection<KnowledgePath>)invocation.getArguments()[0])) {
					if (getStrippedPath(model.exchangeParamCoordIn.getKnowledgePath()).equals(kp)) {
						result.setValue(kp, shadow1InValue);
					} else if (getStrippedPath(model.exchangeParamCoordInOut.getKnowledgePath()).equals(kp)) {
						result.setValue(kp, shadow1InOutValue);
					}
				}
				
				return result;
			}
		});

		when(shadowKnowledgeManager2.get(anyCollectionOf(KnowledgePath.class))).then(new Answer<ValueSet>() {
			public ValueSet answer(InvocationOnMock invocation) {
				ValueSet result = new ValueSet();
				
				// Use inValue and inOutValue as the responses of the knowledge manager
				for (KnowledgePath kp : ((Collection<KnowledgePath>)invocation.getArguments()[0])) {
					if (getStrippedPath(model.exchangeParamCoordIn.getKnowledgePath()).equals(kp)) {
						result.setValue(kp, shadow2InValue);
					} else if (getStrippedPath(model.exchangeParamCoordInOut.getKnowledgePath()).equals(kp)) {
						result.setValue(kp, shadow2InOutValue);
					}
				}
				
				return result;
			}
		});

		model.setKnowledgeManager(knowledgeManager);
		model.setOtherKnowledgeManagersAccess(shadowReplicasAccess);

		when(kmContainer.hasReplica(anyString())).thenReturn(true);
		
		this.task = new EnsembleTask(model.ensembleController, scheduler, architectureObserver, kmContainer, ratingsManager);
	}
	
	@Test
	public void testSchedulingPeriod() {
		// GIVEN an EnsembleTask initialized with an InstanceEnsemblingController
		// WHEN getSchedulingPeriod is called
		long period = task.getTimeTrigger().getPeriod();
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
		verify(knowledgeManager).register(triggerCaptor.capture(), any(TriggerListener.class));
		// AND the trigger has a knowledge path which omits the member/coordinator prefix
		assertEquals(getStrippedPath(model.ensembleKnowledgeChangeTrigger.getKnowledgePath()), ((KnowledgeChangeTrigger)triggerCaptor.getValue()).getKnowledgePath());
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
	public void testEnsembleTaskInvoke() throws Exception {
		// GIVEN an EnsembleTask initialized with an InstanceEnsemblingController
		model.resetMembershipMethodCallCounter();
		model.resetExchangeMethodCallCounter();
		
		// WHEN invoke (with periodic trigger) is called on the task
		task.invoke(model.ensemblePeriodicTrigger);
		// THEN it gets the needed local knowledge
		// 6x = 2x membership (COORD), 2x membership (MEMBER), 1x exchange (COORD), 1x exchange (MEMBER)
		verify(knowledgeManager, times(6)).get(anyCollectionOf(KnowledgePath.class));
		// AND it retrieves the other knowledge managers
		verify(shadowReplicasAccess).getShadowKnowledgeManagers();
		// AND it gets knowledge from them
		verify(shadowKnowledgeManager1, times(4)).get(anyCollectionOf(KnowledgePath.class));
		verify(shadowKnowledgeManager2, times(2)).get(anyCollectionOf(KnowledgePath.class));		
		// AND it executes the membership (there are four different combinations for an ensemble with the local component acting as coordinator or as a member)
		assertTrue(model.getMembershipMethodCallCounter() == 4);
		// AND it executes knowledge exchange for those for which the membership was satisfied (there are two ensembles - one in which the local component acts as a coordinator
		// and one in which it acts as a member)
		assertTrue(model.getExchangeMethodCallCounter() == 2);
		// AND it updates the instance knowledge with outputs of the knowledge exchange
		ArgumentCaptor<ChangeSet> changeSetCaptor = ArgumentCaptor.forClass(ChangeSet.class);
		verify(knowledgeManager, times(2)).update(changeSetCaptor.capture(), anyString());
		ChangeSet cs = changeSetCaptor.getValue();
		assertEquals(cs.getValue(getStrippedPath(model.exchangeParamCoordInOut.getKnowledgePath())), expectedLocalInOutValue.value);
		assertEquals(cs.getValue(getStrippedPath(model.exchangeParamCoordOut.getKnowledgePath())), expectedLocalOutValue.value);
		assertTrue(cs.getDeletedReferences().isEmpty());
		assertTrue(cs.getUpdatedReferences().size() == 2);
	}
}

