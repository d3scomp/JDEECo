package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * KnowledgeManager testing.
 * The test checks the correctness of adding a new KnowledgeReference with its value or 
 * changing the value of existing KnowledgeReference. Also, it checks if the values are 
 * deleted in correct way.    
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeSet.KnowledgeValue;

public class KnowledgeManagerTest {
	
	@Mock
	private List<KnowledgeReference> knowledgeReferenceList;
	@Mock
	private ChangeSet changeSet;
	
	private KnowledgeManager knowledgeManager;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.knowledgeManager = new KnowledgeManagerImpl();
	}


	@Test
	public void testUpdate() {
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
		
		// WHEN define a Mock of ChangeSet  
		this.changeSet = mock(ChangeSet.class);
		// THEN define the behavior of the changeSet:
		//     1- the value "1" returned by getValue(knowledgeReferenceUpdate)
		//	   2- the value colUpdate returned by getUpdatedReferences()
		//     3- the value KnowledgeValue.EMPTY returned by getValue(knowledgeReferenceDelete)
		//     4- the value colDelete returned by getDeletedReferences()
		when(changeSet.getValue(knowledgeReferenceUpdate)).thenReturn("1");
		when(changeSet.getUpdatedReferences()).thenReturn(colUpdate);
		when(changeSet.getValue(knowledgeReferenceDelete)).thenReturn(KnowledgeValue.EMPTY);
		when(changeSet.getDeletedReferences()).thenReturn(colDelete);
		
		// WHEN execute the update method in KnowledgeManager
		knowledgeManager.update(changeSet);

		// THEN Check if the KnowledgeSet in the changeSet is updated with the new changes (add/update/delete) 
		assertEquals(true, changeSet.getUpdatedReferences().contains(knowledgeReferenceUpdate));
		assertEquals(true, changeSet.getDeletedReferences().contains(knowledgeReferenceDelete));
		
	}
	
}
