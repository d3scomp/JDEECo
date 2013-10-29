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


import java.util.Collection;
import java.util.LinkedList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.TriggerListener;


public class ReadOnlyKnowledgeManagerTest {
	
	@Mock
	private Trigger trigger;
	@Mock
	private TriggerListener triggerListener;
	@Mock
	private ValueSet valueSet;


	private ReadOnlyKnowledgeManager readOnlyKM;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.trigger = mock(Trigger.class);
		this.triggerListener = mock(TriggerListener.class);
		this.readOnlyKM = new KnowledgeManagerImpl();
	}

	@Test
	public void testGet() {
		// WHEN define an element(s) of the new/updated values  
		KnowledgeReference knowledgeReferenceUpdate = mock(KnowledgeReference.class);
		// THEN add the element(s) to the Update Reference Collection
		Collection<KnowledgeReference> colUpdate= new LinkedList<KnowledgeReference>();
		colUpdate.add(knowledgeReferenceUpdate);

		// WHEN define an element(s) of the deleted values
		KnowledgeReference knowledgeReferenceDelete = mock(KnowledgeReference.class);
		// THEN add the element(s) to the Delete Reference Collection
		Collection<KnowledgeReference> colDelete= new LinkedList<KnowledgeReference>();
		colDelete.add(knowledgeReferenceDelete);

				
		// WHEN define a Mock of ValueSet  
		valueSet = mock(ValueSet.class);
		// THEN define the behavior of the ValueSet:
		//     1- the value "1" returned by getValue(knowledgeReferenceUpdate)
		//	   2- the value colUpdate returned by getUpdatedReferences()
		//     3- the value KnowledgeValue.EMPTY returned by getValue(knowledgeReferenceDelete)
		//     4- the value colDelete returned by getDeletedReferences()
		when(valueSet.getValue(knowledgeReferenceUpdate)).thenReturn("1");
		when(valueSet.getReferences()).thenReturn(colUpdate);
		when(valueSet.getValue(knowledgeReferenceDelete)).thenReturn(null);
		//when(valueSet.getNotFoundReferences()).thenReturn(colDelete);

		// WHEN define a new KnowledgeManager and returned the ValueSet of Updated References
		readOnlyKM = new KnowledgeManagerImpl();
		valueSet = readOnlyKM.get(colUpdate);
		// THEN check if the returned ValueSet has knowledgeReferenceUpdate
		// FIXME: this has to be implemented
		//assertEquals(true, valueSet.getReferences().contains(knowledgeReferenceUpdate));
		
		// WHEN define a new KnowledgeManager returned the ValueSet of Deleted References
		valueSet = readOnlyKM.get(colDelete);
		// THEN check if the returned ValueSet has knowledgeReferenceDelete
		assertEquals(false, valueSet.getReferences().contains(knowledgeReferenceDelete));
	}	
	
	
 	@Test
	public void testRegister() {
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
		readOnlyKM.register(trigger,triggerListener);
		// THEN the triggerListener adds this trigger
		// assertEquals(true,triggerListener.getTriggers().contain(trigger));
		
		verifyNoMoreInteractions(trigger);
	}
	
 	@Test
	public void testUnregister() {
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
		readOnlyKM.unregister(trigger,triggerListener);
		// THEN the triggerListener removes this trigger
		// assertEquals(false,triggerListener.getTriggers().contain(trigger));

		verifyNoMoreInteractions(trigger);

	}
	
}
