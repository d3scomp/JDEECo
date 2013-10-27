package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * ReadOnlyKnowledgeManager testing.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

public class ReadOnlyKnowledgeManagerTest {
	
	@Mock
	private List<KnowledgeReference> knowledgeReferenceList;
	@Mock
	private Trigger trigger;
	@Mock
	private TriggerListener triggerListener;
	@Mock
	private ValueSet valueSet;
	@Mock
	private KnowledgeSet ks;
	
	private ReadOnlyKnowledgeManager readOnlyKM;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.readOnlyKM = new ReadOnlyKnowledgeManagerImpl();
		this.ks = mock(KnowledgeSet.class);
	}

	@Ignore
	@Test
	public void testGet() {
		KnowledgeReference knowledgeReferenceUpdate = mock(KnowledgeReference.class);
		KnowledgeReference knowledgeReferenceDelete = mock(KnowledgeReference.class);
		Collection<KnowledgeReference> colUpdate= new LinkedList<KnowledgeReference>();
		colUpdate.add(knowledgeReferenceUpdate);
		Collection<KnowledgeReference> colDelete= new LinkedList<KnowledgeReference>();
		colDelete.add(knowledgeReferenceDelete);
		
		when(ks.getNonEmptyReferences()).thenReturn(colUpdate);
		when(ks.getNonEmptyReferences()).thenReturn(colDelete);
		
		this.valueSet = new ValueSet(ks);
		
		when(valueSet.getFoundReferences()).thenReturn(colUpdate);
		when(valueSet.getNotFoundReferences()).thenReturn(colDelete);

		valueSet = readOnlyKM.get(colUpdate);
		assertEquals(colUpdate, valueSet.getFoundReferences());

		valueSet = readOnlyKM.get(colDelete);
		assertEquals(colDelete, valueSet.getNotFoundReferences());
		
		verifyNoMoreInteractions(valueSet);
	}	
	
	@Test
	public void testRegister() {
		readOnlyKM.register(trigger,triggerListener);
		verifyNoMoreInteractions(trigger);
	}
}
