package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * ReadOnlyKnowledgeManager testing.
 * The test check the correctness of adding a new KnowledgeReference with its value or 
 * changing the value of existing KnowledgeReference. Also, it checks if the values are 
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

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.TriggerListener;


public class ReadOnlyKnowledgeManagerTest {
		
	private Trigger trigger;	
	private TriggerListener triggerListener;
	private ReadOnlyKnowledgeManager tested;

	
	private ReadOnlyKnowledgeManager createKM(Map<KnowledgeReference, Object> initialKnowledge) {
		return new KnowledgeManagerImpl(initialKnowledge);
	}
	
	@Before
	public void setUp() throws Exception {
		trigger = mock(Trigger.class);
		triggerListener = mock(TriggerListener.class);
		tested = createKM(new HashMap<KnowledgeReference, Object>());
	}

	@Test
	@Ignore
	public void testGet() {
		final KnowledgeReference r = mock(KnowledgeReference.class);
		
		// WHEN the ReadOnlyKnowledgeManager is empty
		// THEN it returns a non-null, empty ValueSet for every KnowledgeRef list.
		ValueSet valueSet = tested.get(Arrays.asList(r));
		assertNotNull(valueSet);
		assertTrue(valueSet.getReferences().isEmpty());
				
		// WHEN the KM contains a value for r only
		final Object v = new Object();
		tested = createKM(new HashMap<KnowledgeReference, Object>() {{ 
	        put(r, v);	        
	    }});
		// THEN get({r, r2}) returns v as value for r (and no other ref. value)
		final KnowledgeReference r2 = mock(KnowledgeReference.class);
		valueSet = tested.get(Arrays.asList(r, r2));
		assertNotNull(valueSet);
		assertEquals(1, valueSet.getReferences().size());
		assertTrue(valueSet.getReferences().contains(r));
		assertFalse(valueSet.getReferences().contains(r2));	
	}	
	
	
	@Test
	public void testRegister() {
		// TODO: has to include testing that the trigger listener is actually
		// notified on trigger
		tested.register(trigger,triggerListener);
		verifyNoMoreInteractions(trigger);
	}
	
	@Test
	public void testUnregister() {
		// TODO: has to include testing that the trigger listener is no longer
		// notified on trigger (set up the KM so that listener was notified
		// at the beginning)
		tested.unregister(trigger, triggerListener);
		verifyNoMoreInteractions(trigger);

	}
	
}
