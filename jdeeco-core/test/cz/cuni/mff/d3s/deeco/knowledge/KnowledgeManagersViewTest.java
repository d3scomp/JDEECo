package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

/**
 * KnowledgeManagersView testing.
 * The test checks the correctness of getting the KnowledgeManagers and register/unregister 
 * the triggers of the others.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */
public class KnowledgeManagersViewTest {
	
	@Mock
	private Trigger trigger;
	@Mock
	private ShadowsTriggerListener shadowsTriggerListener;
	@Mock
	private ValueSet valueSet;
	@Mock
	private ReadOnlyKnowledgeManager km;
 
	private KnowledgeManagersView kmView;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		// FIXME TB: Why do you have the "this." here?
		// FIXME TB: Why don't you instantiate this mock using the annotation as the other mocks above?
		this.kmView =mock(KnowledgeManagersView.class);
	}

	// FIXME TB: The tests below need to be fixed to comply to BDD.
	
 	@Test 
	public void testGetOtherKnowledgeManager(){
		// WHEN km mocks KnowledgeManager and define a Collection of ReadOnlyKnowledgeManagers that do not have km
		this.km = mock(ReadOnlyKnowledgeManager.class);
 		Collection<ReadOnlyKnowledgeManager> colKM = new LinkedList<ReadOnlyKnowledgeManager>();
 		// THEN define the behavior of KnowledgeManagerView :
		//  1- return "colKM" by calling getOthersKnowledgeManagers()
		when(kmView.getOtherKnowledgeManagers()).thenReturn(colKM);
		
		// WHEN call getOthersKnowledgeManagers()
		// THEN check the returned collection do not have km 
		assertEquals(false, kmView.getOtherKnowledgeManagers().contains(km));
		
	}
	
	@Ignore
 	@Test
	public void testRegisterOthers() {
		// WHEN define a new for trigger
		// FIXME TB: Why do you call the mock here when the mock has been already created because of the annotation above and the call to initMocks?
		this.trigger = mock(Trigger.class);
		// THEN add the trigger(s) to a Collection
		Collection<Trigger> colTriggers= new LinkedList<Trigger>();
		colTriggers.add(trigger);
		
		// WHEN mock new TriggerListener
		// FIXME TB: Why do you call the mock here when the mock has been already created because of the annotation above and the call to initMocks?
		this.shadowsTriggerListener = mock(ShadowsTriggerListener.class);
		// THEN define the behavior of the TriggerListener:
		//	 the value "colTriggers" returned by getTriggers()
		//when(triggerListener.getTriggers()).thenReturn(colTriggers);
		
		
		// WHEN registering a trigger to triggerListener
		kmView.register(trigger,shadowsTriggerListener);
		// THEN the triggerListener adds this trigger
		// assertEquals(true,triggerListener.getTriggers().contain(trigger));
		
		verifyNoMoreInteractions(trigger);
	}
	
	@Ignore
 	@Test
	public void testUnregisterOthers() {
		// WHEN define a new for trigger   
		// FIXME TB: Why do you call the mock here when the mock has been already created because of the annotation above and the call to initMocks?
		this.trigger = mock(Trigger.class);
		// THEN define a Collection of Triggers without the currentTrigger
		Collection<Trigger> colTriggers= new LinkedList<Trigger>();
		
		// WHEN mock new TriggerListener
		// FIXME TB: Why do you call the mock here when the mock has been already created because of the annotation above and the call to initMocks?
		this.shadowsTriggerListener = mock(ShadowsTriggerListener.class);
		// THEN define the behavior of the TriggerListener:
		//	 the value "colTriggers" returned by getTriggers()
		//when(triggerListener.getTriggers()).thenReturn(colTriggers);
		
		
		// WHEN registering a trigger to triggerListener
		kmView.unregister(trigger,shadowsTriggerListener);
		// THEN the triggerListener removes this trigger
		// assertEquals(false,triggerListener.getTriggers().contain(trigger));

		verifyNoMoreInteractions(trigger);

	}
	
}
