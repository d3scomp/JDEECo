package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeSet.KnowledgeValue;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

public class KnowledgeManagerTest {
	
	@Mock
	private List<KnowledgeReference> knowledgeReferenceList;
	@Mock
	private Trigger trigger;
	@Mock
	private KnowledgeReference knowledgeReferenceObj;

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
		// Specify behavior for mock objects
		// Can we define something more specific than just Object for this return type, it seems to broad.
		
		knowledgeManager.update(changeSet);

		Object obj=new Object();
		assertEquals(0, changeSet.getUpdatedReferences().size());
		changeSet.setValue(knowledgeReferenceObj, obj);
		assertNotNull(changeSet.getUpdatedReferences());
		assertEquals(1, changeSet.getUpdatedReferences().size());
		assertEquals(true, changeSet.getUpdatedReferences().contains(knowledgeReferenceObj));

		assertEquals(0, changeSet.getDeletedReferences().size());
		changeSet.setDeleted(knowledgeReferenceObj);
		assertNotNull(changeSet.getDeletedReferences());
		assertEquals(1, changeSet.getDeletedReferences().size());
		assertEquals(true, changeSet.getDeletedReferences().contains(knowledgeReferenceObj));
	
		verifyNoMoreInteractions(knowledgeReferenceObj);
	}
	
	
	@Test
	public void testGet() {
		
		knowledgeReferenceList.add(knowledgeReferenceObj);
		Object value = new Object();
		
		changeSet.setValue(knowledgeReferenceObj, "1");
		knowledgeManager.update(changeSet);
		assertNotNull(knowledgeManager.get(knowledgeReferenceList));
		assertEquals("1", knowledgeManager.get(knowledgeReferenceList).getValue(knowledgeReferenceObj));
			
		changeSet.setValue(knowledgeReferenceObj, KnowledgeValue.EMPTY);
		knowledgeManager.update(changeSet);
		assertEquals(KnowledgeValue.EMPTY, knowledgeManager.get(knowledgeReferenceList).getValue(knowledgeReferenceObj));
		
		changeSet.setDeleted(knowledgeReferenceObj);
		assertNull(knowledgeManager.get(knowledgeReferenceList).getValue(knowledgeReferenceObj));
		
		verifyNoMoreInteractions(knowledgeReferenceObj);
		verifyNoMoreInteractions(knowledgeReferenceList);
}
	
	@Test
	public void testRegister() {
		
		knowledgeManager.register(trigger);
//		verifyNoMoreInteractions(trigger);
	}

}
