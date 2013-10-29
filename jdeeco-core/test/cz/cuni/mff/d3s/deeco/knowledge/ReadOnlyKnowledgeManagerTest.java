package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * ReadOnlyKnowledgeManager testing.
 * The test check the correctness of adding a new KnowledgePath with its value or 
 * changing the value of existing KnowledgePath. Also, it checks if the values are 
 * deleted in correct way. 
 * It checks also the bind and unbind for the trigger with triggerListner.   
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;


public class ReadOnlyKnowledgeManagerTest {
		
	private Trigger trigger;	
	private TriggerListener triggerListener;
	private ReadOnlyKnowledgeManager tested;

	
	private ReadOnlyKnowledgeManager createKM(Map<KnowledgePath, Object> initialKnowledge) {
		return new KnowledgeManagerImpl(initialKnowledge);
	}
	
	@Before
	public void setUp() throws Exception {
		trigger = mock(Trigger.class);
		triggerListener = mock(TriggerListener.class);
		tested = createKM(new HashMap<KnowledgePath, Object>());
	}

	@Test
	@Ignore
	public void testGet() {
		final KnowledgePath r = mock(KnowledgePath.class);
		
		// WHEN the ReadOnlyKnowledgeManager is empty
		// THEN it returns a non-null, empty ValueSet for every KnowledgeRef list.
		ValueSet valueSet = tested.get(Arrays.asList(r));
		assertNotNull(valueSet);
		assertTrue(valueSet.getKnowledgePaths().isEmpty());
				
		// WHEN the KM contains a value for r only
		final Object v = new Object();
		tested = createKM(new HashMap<KnowledgePath, Object>() {{ 
	        put(r, v);	        
	    }});
		// THEN get({r, r2}) returns v as value for r (and no other ref. value)
		final KnowledgePath r2 = mock(KnowledgePath.class);
		valueSet = tested.get(Arrays.asList(r, r2));
		assertNotNull(valueSet);
		assertEquals(1, valueSet.getKnowledgePaths().size());
		assertTrue(valueSet.getKnowledgePaths().contains(r));
		assertFalse(valueSet.getKnowledgePaths().contains(r2));	
	}	
	
	
 	@Test
	public void testRegister() {
		// TODO: has to include testing that the trigger listener is actually
		// notified on trigger

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
		tested.register(trigger,triggerListener);
		// THEN the triggerListener adds this trigger
		// assertEquals(true,triggerListener.getTriggers().contain(trigger));
		

		verifyNoMoreInteractions(trigger);
	}
	
 	@Test
	public void testUnregister() {

		// TODO: has to include testing that the trigger listener is no longer
		// notified on trigger (set up the KM so that listener was notified
		// at the beginning)

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
		tested.unregister(trigger,triggerListener);
		// THEN the triggerListener removes this trigger
		// assertEquals(false,triggerListener.getTriggers().contain(trigger));

		verifyNoMoreInteractions(trigger);

	}
	
}
