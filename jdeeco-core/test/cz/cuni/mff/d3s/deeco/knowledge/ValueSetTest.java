package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.LinkedList;


import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeSet.KnowledgeValue;

/**
 *  
 * @author Keznikl
 *
 */
public class ValueSetTest {

	ValueSet tested;
	
	
	@Before
	public void setUp() throws Exception {	
		tested = new ValueSet();
	}



	@Test
	public void testGetReferences() {
		// WHEN a ValueSet is empty
		// THEN there are no references
		assertNotNull(tested.getReferences());
		assertEquals(0, tested.getReferences().size());
		
		// WHEN we insert the first value for a reference
		KnowledgeReference r = mock(KnowledgeReference.class);
		Object v = new Object();
		tested.setValue(r, v);		
		// THEN it is the sole reference returned by getReferences()
		assertEquals(1, tested.getReferences().size());
		assertEquals(true, tested.getReferences().contains(r));
		assertEquals(v, tested.getValue(r));
		
		// WHEN we insert a value for another reference
		KnowledgeReference r2 = mock(KnowledgeReference.class);
		Object v2 = new Object();
		tested.setValue(r2, v2);
		// THEN the getReferences() includes both references
		assertEquals(2, tested.getReferences().size());
		assertEquals(true, tested.getReferences().contains(r));
		assertEquals(true, tested.getReferences().contains(r2));
		assertEquals(v2, tested.getValue(r2));
		
		// WHEN we insert another value for a previous reference
		tested.setValue(r2, v);
		// THEN there are still two references returned by
		// getUpdatedReferences(), only the value of one reference changed
		assertEquals(2, tested.getReferences().size());
		assertEquals(true, tested.getReferences().contains(r));
		assertEquals(true, tested.getReferences().contains(r2));
		assertEquals(v, tested.getValue(r));
		assertEquals(v, tested.getValue(r2));		
	}
	
	@Test
	public void testGettersSetters() {
		KnowledgeReference r = mock(KnowledgeReference.class);
		// WHEN the ref. is not set
		// THEN getValue() returns null
		assertNull(tested.getValue(r));
				
		// WHEN the value for a ref. is set
		Object v = new Object();
		tested.setValue(r, v);
		// THEN getValue() returns the value
		assertEquals(v, tested.getValue(r));
		
		// WHEN the value for a ref. is set to null
		tested.setValue(r, null);
		// THEN getValue() returns null
		assertNull(tested.getValue(r));		
	}
	

}
