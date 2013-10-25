package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import org.junit.Before;
import org.junit.Test;

/**
 * Example of black-box testing.
 * 
 * @author Keznikl
 *
 */
public class ChangeSetTest {

	ChangeSet blackbox;
	
	@Before
	public void setUp() throws Exception {
		blackbox = new ChangeSet();
	}
	
	
	@Test
	public void testGetUpdatedReferences() {
		// WHEN a ChangeSet is new 
		// THEN getUpdatedReferences() returns an empty collection
		assertNotNull(blackbox.getUpdatedReferences());
		assertEquals(0, blackbox.getUpdatedReferences().size());
		
		// WHEN we insert the first value for a reference
		KnowledgeReference r = mock(KnowledgeReference.class);
		Object v = new Object();
		blackbox.setValue(r, v);
		// THEN it is the sole reference returned by getUpdatedReferences()
		assertEquals(1, blackbox.getUpdatedReferences().size());
		assertEquals(true, blackbox.getUpdatedReferences().contains(r));
		assertEquals(v, blackbox.getValue(r));
		
		// WHEN we insert a value for another reference
		KnowledgeReference r2 = mock(KnowledgeReference.class);
		Object v2 = new Object();
		blackbox.setValue(r2, v2);
		// THEN the getUpdatedReferences() includes both references
		assertEquals(2, blackbox.getUpdatedReferences().size());
		assertEquals(true, blackbox.getUpdatedReferences().contains(r));
		assertEquals(true, blackbox.getUpdatedReferences().contains(r2));
		assertEquals(v2, blackbox.getValue(r2));
		
		// WHEN we insert another value for a previous reference
		blackbox.setValue(r2, v);
		// THEN there are still two references returned by
		// getUpdatedReferences(), only the value of one reference changed
		assertEquals(2, blackbox.getUpdatedReferences().size());
		assertEquals(true, blackbox.getUpdatedReferences().contains(r));
		assertEquals(true, blackbox.getUpdatedReferences().contains(r2));
		assertEquals(v, blackbox.getValue(r));
		assertEquals(v, blackbox.getValue(r2));
		
		// the whole process of setting values for refs. does not influence
		// the list of deleted refs.
		assertEquals(0, blackbox.getDeletedReferences().size());
	}
	
	@Test
	public void testGetDeletedReferences() {
		// WHEN a ChangeSet is new 
		// THEN getUpdatedReferences() returns an empty collection
		assertNotNull(blackbox.getDeletedReferences());
		assertEquals(0, blackbox.getDeletedReferences().size());
		
		// WHEN we set the first reference as deleted
		KnowledgeReference r = mock(KnowledgeReference.class);		
		blackbox.setDeleted(r);
		// THEN it is the sole reference returned by getDeletedReferences()
		assertEquals(1, blackbox.getDeletedReferences().size());
		assertEquals(true, blackbox.getDeletedReferences().contains(r));
		// AND the value returned for the ref. is null
		assertNull(blackbox.getValue(r));
		
		// WHEN we set another reference as deleted
		KnowledgeReference r2 = mock(KnowledgeReference.class);		
		blackbox.setDeleted(r2);
		// THEN the getDeletedReferences() includes both references
		assertEquals(2, blackbox.getDeletedReferences().size());
		assertEquals(true, blackbox.getDeletedReferences().contains(r));
		assertEquals(true, blackbox.getDeletedReferences().contains(r2));
		
		// WHEN we set a value for a previously-deleted reference
		Object v = new Object();
		blackbox.setValue(r2, v);
		// THEN there is ane references returned by
		// getDeletedReferences() and the other by getUpdatedReferences()
		assertEquals(1, blackbox.getDeletedReferences().size());
		assertEquals(true, blackbox.getDeletedReferences().contains(r));
		assertEquals(1, blackbox.getUpdatedReferences().size());
		assertEquals(true, blackbox.getUpdatedReferences().contains(r2));
		assertEquals(v, blackbox.getValue(r2));
		
		// WHEN we set the previously-re-set reference as deleted
		blackbox.setDeleted(r2);
		// THEN both refs. are returned as deleted and none is updated
		assertEquals(2, blackbox.getDeletedReferences().size());
		assertEquals(true, blackbox.getDeletedReferences().contains(r));
		assertEquals(true, blackbox.getDeletedReferences().contains(r2));
		assertEquals(0, blackbox.getUpdatedReferences().size());
	}
	
	@Test
	public void testGettersSetters() {
		KnowledgeReference r = mock(KnowledgeReference.class);
		// WHEN the ref. is not set
		// THEN getValue() returns null
		assertNull(blackbox.getValue(r));
				
		// WHEN the value for a ref. is set
		Object v = new Object();
		blackbox.setValue(r, v);
		// THEN getValue() returns the value
		assertEquals(v, blackbox.getValue(r));
		
		// WHEN the ref. is set as deleted
		blackbox.setDeleted(r);
		// THEN getValue() returns null
		assertNull(blackbox.getValue(r));		
	}
}
