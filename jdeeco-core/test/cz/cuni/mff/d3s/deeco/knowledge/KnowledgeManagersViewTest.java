package cz.cuni.mff.d3s.deeco.knowledge;

/**
 * KnowledgeManagersView testing.
 * The test checks the correctness of getting the KnowledgeManagers and register/unregister 
 * the triggers of the others.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */

import static org.junit.Assert.*;
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
import cz.cuni.mff.d3s.deeco.task.TriggerListener;


public class KnowledgeManagersViewTest {
	
	@Mock
	private Trigger trigger;
	@Mock
	private TriggerListener triggerListener;
	@Mock
	private ValueSet valueSet;
	@Mock
	private ReadOnlyKnowledgeManager km;
 
	private KnowledgeManagersView kmView;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.kmView =mock(KnowledgeManagersView.class);
	}

 	@Test 
	public void testGetOtherKnowledgeManager(){
		// WHEN km mocks KnowledgeManager and define a Collection of ReadOnlyKnowledgeManagers that do not have km
		this.km = mock(ReadOnlyKnowledgeManager.class);
 		Collection<ReadOnlyKnowledgeManager> colKM = new LinkedList<ReadOnlyKnowledgeManager>();
 		// THEN define the behavior of KnowledgeManagerView :
		//  1- return "colKM" by calling getOthersKnowledgeManagers()
		when(kmView.getOthersKnowledgeManagers()).thenReturn(colKM);
		
		// WHEN call getOthersKnowledgeManagers()
		// THEN check the returned collection do not have km 
		assertEquals(false, kmView.getOthersKnowledgeManagers().contains(km));
		
	}
	
	@Ignore
 	@Test
	public void testRegisterOthers() {
		// WHEN define a new for trigger   
		this.trigger = mock(Trigger.class);
		// THEN add the trigger(s) to a Collection
		Collection<Trigger> colTriggers= new LinkedList<Trigger>();
		colTriggers.add(trigger);
		
		// WHEN mock new TriggerListener
		this.triggerListener = mock(TriggerListener.class);
		// THEN define the behavior of the TriggerListener:
		//	 the value "colTriggers" returned by getTriggers()
		//when(triggerListener.getTriggers()).thenReturn(colTriggers);
		
		
		// WHEN registering a trigger to triggerListener
		kmView.register(trigger,triggerListener);
		// THEN the triggerListener adds this trigger
		// assertEquals(true,triggerListener.getTriggers().contain(trigger));
		
		verifyNoMoreInteractions(trigger);
	}
	
	@Ignore
 	@Test
	public void testUnregisterOthers() {
		// WHEN define a new for trigger   
		this.trigger = mock(Trigger.class);
		// THEN define a Collection of Triggers without the currentTrigger
		Collection<Trigger> colTriggers= new LinkedList<Trigger>();
		
		// WHEN mock new TriggerListener
		this.triggerListener = mock(TriggerListener.class);
		// THEN define the behavior of the TriggerListener:
		//	 the value "colTriggers" returned by getTriggers()
		//when(triggerListener.getTriggers()).thenReturn(colTriggers);
		
		
		// WHEN registering a trigger to triggerListener
		kmView.unregister(trigger,triggerListener);
		// THEN the triggerListener removes this trigger
		// assertEquals(false,triggerListener.getTriggers().contain(trigger));

		verifyNoMoreInteractions(trigger);

	}
	
}
