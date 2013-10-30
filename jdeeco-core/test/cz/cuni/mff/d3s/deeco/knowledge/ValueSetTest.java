package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
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
		assertNotNull(tested.getKnowledgePaths());
		assertEquals(0, tested.getKnowledgePaths().size());
		
		// WHEN we insert the first value for a reference
		KnowledgePath r = mock(KnowledgePath.class);
		Object v = new Object();
		tested.setValue(r, v);		
		// THEN it is the sole reference returned by getReferences()
		assertEquals(1, tested.getKnowledgePaths().size());
		assertEquals(true, tested.getKnowledgePaths().contains(r));
		assertEquals(v, tested.getValue(r));
		
		// WHEN we insert a value for another reference
		KnowledgePath r2 = mock(KnowledgePath.class);
		Object v2 = new Object();
		tested.setValue(r2, v2);
		// THEN the getReferences() includes both references
		assertEquals(2, tested.getKnowledgePaths().size());
		assertEquals(true, tested.getKnowledgePaths().contains(r));
		assertEquals(true, tested.getKnowledgePaths().contains(r2));
		assertEquals(v2, tested.getValue(r2));
		
		// WHEN we insert another value for a previous reference
		tested.setValue(r2, v);
		// THEN there are still two references returned by
		// getReferences(), only the value of one reference changed
		assertEquals(2, tested.getKnowledgePaths().size());
		assertEquals(true, tested.getKnowledgePaths().contains(r));
		assertEquals(true, tested.getKnowledgePaths().contains(r2));
		assertEquals(v, tested.getValue(r));
		assertEquals(v, tested.getValue(r2));		
	}
	
	@Test
	public void testGettersSetters() {
		KnowledgePath r = mock(KnowledgePath.class);
		// WHEN the ref. is not set
		// THEN getValue() returns null and the reference is not among the getReferences()
		assertNull(tested.getValue(r));
		assertFalse(tested.getKnowledgePaths().contains(r));

				
		// WHEN the value for a ref. is set
		Object v = new Object();
		tested.setValue(r, v);
		// THEN getValue() returns the value
		assertEquals(v, tested.getValue(r));
		
		// WHEN the value for a ref. is set to null
		tested.setValue(r, null);
		// THEN getValue() returns null and the ref. is among the getReferences()
		assertNull(tested.getValue(r));		
		assertTrue(tested.getKnowledgePaths().contains(r));
		
	}
	

}
