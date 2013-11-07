package cz.cuni.mff.d3s.deeco.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.SampleRuntimeModel;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public class ProcessTaskTest {

        private SampleRuntimeModel model;
        
        @Mock
        private KnowledgeManager knowledgeManager;
        @Mock
        private TaskTriggerListener taskTriggerListener;
        @Captor
        private ArgumentCaptor<TriggerListener> knowledgeManagerTriggerListenerCaptor;
        @Mock
        private Scheduler scheduler;

        private Task task;
        
        private Integer inValue;
        private Integer inOutValue;

        private ParamHolder<Integer> expectedInOutValue;
        private ParamHolder<Integer> expectedOutValue;

        @Before
        public void setUp() throws Exception {
                initMocks(this);

                model = new SampleRuntimeModel();
                
                inValue = 21;
                inOutValue = 108;
                expectedInOutValue = new ParamHolder<Integer>(108);
                expectedOutValue = new ParamHolder<Integer>(0);                
                SampleRuntimeModel.processMethod(inValue, expectedOutValue, expectedInOutValue);
                
                doNothing().when(knowledgeManager).register(eq(model.processKnowledgeChangeTrigger), knowledgeManagerTriggerListenerCaptor.capture());
                
                when(knowledgeManager.get(anyCollectionOf(KnowledgePath.class))).then(new Answer<ValueSet>() {
                        public ValueSet answer(InvocationOnMock invocation) {
                                ValueSet result = new ValueSet();
                                
                                // Use inValue and inOutValue as the responses of the knowledge manager
                                for (KnowledgePath kp : ((Collection<KnowledgePath>)invocation.getArguments()[0])) {
                                        if (kp.equals(model.processParamIn.getKnowledgePath())) {
                                                result.setValue(kp, inValue);
                                        } else if (kp.equals(model.processParamInOut.getKnowledgePath())) {
                                                result.setValue(kp, inOutValue);
                                        }
                                }
                                
                                return result;
                        }
                });
                
                model.setKnowledgeManager(knowledgeManager);
                
                this.task = new ProcessTask(model.process, scheduler);
        }
        
        @Test
        public void testSchedulingPeriod() {
                // GIVEN a ProcessTask initialized with an ComponentProcess
                // WHEN getSchedulingPeriod is called
                long period = task.getPeriodicTrigger().getPeriod();
                // THEN it returns the period specified in the model
                assertEquals(model.processPeriodicTrigger.getPeriod(), period);
        }
        
        @Test
        public void testTriggerIsDeliveredOnlyWhenListenerIsRegistered() {
                // GIVEN a ProcessTask initialized with an ComponentProcess
                // WHEN a listener (i.e. scheduler) is registered at the task
                task.setTriggerListener(taskTriggerListener);
                // THEN the task registers a trigger listener on the knowledge manager
                verify(knowledgeManager).register(eq(model.processKnowledgeChangeTrigger), any(TriggerListener.class));
                
                // WHEN a trigger comes from the knowledge manager
                knowledgeManagerTriggerListenerCaptor.getValue().triggered(model.processKnowledgeChangeTrigger);
                // THEN the task calls the registered listener (i.e. the scheduler)
                verify(taskTriggerListener).triggered(task, model.processKnowledgeChangeTrigger);
                
                // WHEN the listener (i.e. the scheduler) is unregistered
                task.unsetTriggerListener();
                // THEN the trigger is unregistered at the knowledge manager
                verify(knowledgeManager).unregister(model.processKnowledgeChangeTrigger, knowledgeManagerTriggerListenerCaptor.getValue());
                
                // WHEN a spurious trigger comes from the knowledge manager
                knowledgeManagerTriggerListenerCaptor.getValue().triggered(model.processKnowledgeChangeTrigger);                
                // THEN it is not propagated to the listener (i.e. the scheduler)
                verifyNoMoreInteractions(taskTriggerListener);
        }
        
        @Test
        public void testProcessTaskInvoke() throws Exception {
                // GIVEN a ProcessTask initialized with an ComponentProcess
                model.resetProcessMethodCallCounter();
                
                // WHEN invoke on the task is called
                task.invoke(null);
                
                // THEN it gets the knowledge needed for execution of the task
                verify(knowledgeManager).get(anyCollectionOf(KnowledgePath.class));
                
                // AND it executes the process method once
                assertTrue(model.getProcessMethodCallCounter() == 1);
                
                // AND it updates knowledge with only the outputs of the process method
                ArgumentCaptor<ChangeSet> changeSetCaptor = ArgumentCaptor.forClass(ChangeSet.class);
                verify(knowledgeManager).update(changeSetCaptor.capture());
                ChangeSet cs = changeSetCaptor.getValue();
                assertEquals(cs.getValue(model.processParamInOut.getKnowledgePath()), expectedInOutValue.value);
                assertEquals(cs.getValue(model.processParamOut.getKnowledgePath()), expectedOutValue.value);
                assertTrue(cs.getDeletedReferences().isEmpty());
                assertTrue(cs.getUpdatedReferences().size() == 2);
        }        
}