package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * Example of black-box testing.
 * 
 * @author Keznikl
 *
 */
public class ChangeSetTest {

	ChangeSet tested;
	
	@Before
	public void setUp() throws Exception {
		tested = new ChangeSet();
	}
	
	
	@Test
	public void testGetUpdatedReferences() {
		// WHEN a ChangeSet is new 
		// THEN getUpdatedReferences() returns an empty collection
		assertNotNull(tested.getUpdatedReferences());
		assertEquals(0, tested.getUpdatedReferences().size());
		
		// WHEN we insert the first value for a reference
		KnowledgePath r = mock(KnowledgePath.class);
		Object v = new Object();
		tested.setValue(r, v);
		// THEN it is the sole reference returned by getUpdatedReferences()
		assertEquals(1, tested.getUpdatedReferences().size());
		assertEquals(true, tested.getUpdatedReferences().contains(r));
		assertEquals(v, tested.getValue(r));
		
		// WHEN we insert a value for another reference
		KnowledgePath r2 = mock(KnowledgePath.class);
		Object v2 = new Object();
		tested.setValue(r2, v2);
		// THEN the getUpdatedReferences() includes both references
		assertEquals(2, tested.getUpdatedReferences().size());
		assertEquals(true, tested.getUpdatedReferences().contains(r));
		assertEquals(true, tested.getUpdatedReferences().contains(r2));
		assertEquals(v2, tested.getValue(r2));
		
		// WHEN we insert another value for a previous reference
		tested.setValue(r2, v);
		// THEN there are still two references returned by
		// getUpdatedReferences(), only the value of one reference changed
		assertEquals(2, tested.getUpdatedReferences().size());
		assertEquals(true, tested.getUpdatedReferences().contains(r));
		assertEquals(true, tested.getUpdatedReferences().contains(r2));
		assertEquals(v, tested.getValue(r));
		assertEquals(v, tested.getValue(r2));
		
		// the whole process of setting values for refs. does not influence
		// the list of deleted refs.
		assertEquals(0, tested.getDeletedReferences().size());
	}
	
	@Test
	public void testGetDeletedReferences() {
		// WHEN a ChangeSet is new 
		// THEN getUpdatedReferences() returns an empty collection
		assertNotNull(tested.getDeletedReferences());
		assertEquals(0, tested.getDeletedReferences().size());
		
		// WHEN we set the first reference as deleted
		KnowledgePath r = mock(KnowledgePath.class);		
		tested.setDeleted(r);
		// THEN it is the sole reference returned by getDeletedReferences()
		assertEquals(1, tested.getDeletedReferences().size());
		assertEquals(true, tested.getDeletedReferences().contains(r));
		// AND the value returned for the ref. is null
		assertNull(tested.getValue(r));
		
		// WHEN we set another reference as deleted
		KnowledgePath r2 = mock(KnowledgePath.class);		
		tested.setDeleted(r2);
		// THEN the getDeletedReferences() includes both references
		assertEquals(2, tested.getDeletedReferences().size());
		assertEquals(true, tested.getDeletedReferences().contains(r));
		assertEquals(true, tested.getDeletedReferences().contains(r2));
		
		// WHEN we set a value for a previously-deleted reference
		Object v = new Object();
		tested.setValue(r2, v);
		// THEN there is ane references returned by
		// getDeletedReferences() and the other by getUpdatedReferences()
		assertEquals(1, tested.getDeletedReferences().size());
		assertEquals(true, tested.getDeletedReferences().contains(r));
		assertEquals(1, tested.getUpdatedReferences().size());
		assertEquals(true, tested.getUpdatedReferences().contains(r2));
		assertEquals(v, tested.getValue(r2));
		
		// WHEN we set the previously-re-set reference as deleted
		tested.setDeleted(r2);
		// THEN both refs. are returned as deleted and none is updated
		assertEquals(2, tested.getDeletedReferences().size());
		assertEquals(true, tested.getDeletedReferences().contains(r));
		assertEquals(true, tested.getDeletedReferences().contains(r2));
		assertEquals(0, tested.getUpdatedReferences().size());
	}
	
	@Test
	public void testGettersSetters() {
		KnowledgePath r = mock(KnowledgePath.class);
		// WHEN the ref. is not set
		// THEN getValue() returns null and the reference is not among the
		// getDeletedReferences() nor getUpdatedReferences()
		assertNull(tested.getValue(r));
		assertFalse(tested.getUpdatedReferences().contains(r));
		assertFalse(tested.getDeletedReferences().contains(r));
				
		// WHEN the value for a ref. is set
		Object v = new Object();
		tested.setValue(r, v);
		// THEN getValue() returns the value and the reference is among
		// getUpdatedReferences() and not among getDeletedReferences()
		assertEquals(v, tested.getValue(r));
		assertTrue(tested.getUpdatedReferences().contains(r));
		assertFalse(tested.getDeletedReferences().contains(r));

		
		// WHEN the ref. is set as deleted
		tested.setDeleted(r);
		// THEN getValue() returns null and the reference is among
		// getDeletedReferences() and not among getUpdatedReferences()
		assertNull(tested.getValue(r));		
		assertFalse(tested.getUpdatedReferences().contains(r));
		assertTrue(tested.getDeletedReferences().contains(r));
	}
}
