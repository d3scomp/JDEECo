package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * KnowledgeManager testing.
 * The test check the correctness of adding a new value or changing the value of existing
 * KnowledgeReference. Also, it checks if the values are deleted in correct way.    
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
		this.changeSet = mock(ChangeSet.class);
	}


	@Test
	public void testUpdate() {
		// Define a Collection of the new/updated values 
		KnowledgeReference knowledgeReferenceUpdate = mock(KnowledgeReference.class);
		Collection<KnowledgeReference> colUpdate= new LinkedList<KnowledgeReference>();
		colUpdate.add(knowledgeReferenceUpdate);
		// Define the Collection of the deleted values 
		KnowledgeReference knowledgeReferenceDelete = mock(KnowledgeReference.class);
		Collection<KnowledgeReference> colDelete= new LinkedList<KnowledgeReference>();
		colDelete.add(knowledgeReferenceDelete);
		
		// Define the behavior of the "changeSet"
		// which has inside it a KnowledgeSet that saves both previous collections.
		when(changeSet.getUpdatedReferences()).thenReturn(colUpdate);
		when(changeSet.getDeletedReferences()).thenReturn(colDelete);
		when(changeSet.getValue(knowledgeReferenceUpdate)).thenReturn("1");
		when(changeSet.getValue(knowledgeReferenceDelete)).thenReturn(KnowledgeValue.EMPTY);
		
		// execute the update method in KnowledgeManager
		knowledgeManager.update(changeSet);

		// Check if the KnowledgeSet in the changeSet is updated with the new changes (add/update/delete) 
		assertEquals(true, changeSet.getUpdatedReferences().contains(knowledgeReferenceUpdate));
		assertEquals(true, changeSet.getDeletedReferences().contains(knowledgeReferenceDelete));
		
	}
	
}
