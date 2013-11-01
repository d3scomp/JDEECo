package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * KnowledgeManagerRegistry testing.
 * The test checks the correctness of adding/removing/getting the KnowledgeManagers of locals and shadows.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */
public class KnowledgeManagerRegistryTest {
	
	@Mock
	KnowledgeManager km;
	@Mock
	Task localTask;
	@Mock
	Task shadowTask;
	
	private KnowledgeManagerRegistry kmRegistry;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
 		this.km = mock(KnowledgeManager.class);
		this.kmRegistry = mock(KnowledgeManagerRegistry.class);
	}

	@Test 
	public void testCreateNewLocalKnowledgeManager(){

		// WHEN create a new Local Knowledge Manager by calling createLocal() 
		when(kmRegistry.createLocal()).thenReturn(km);
		km = kmRegistry.createLocal();

		// THEN a new local instance of knowledge manager is added to the locals and returned
		assertNotNull(km);
		assertEquals(true, km instanceof KnowledgeManager);
	}
	
 	@Test 
	public void testCreateNewShadowKnowledgeManager(){
		
 		// WHEN create a new shadow Knowledge Manager by calling createShadow()
 		when(kmRegistry.createShadow()).thenReturn(km);
		km = kmRegistry.createShadow();

		// THEN a new shadow instance of knowledge manager is added to the shadows and returned
		assertNotNull(km);
		assertEquals(true, km instanceof KnowledgeManager);
		
	}
	
	@Test 
	public void testLocalKnowledgeManagerIsRemoved(){

		// WHEN call removeLocal(KnowledgeManager km)
		Collection<KnowledgeManager> colLocalKM = new LinkedList<KnowledgeManager>();
		when(kmRegistry.getLocals()).thenReturn(colLocalKM);
		kmRegistry.removeLocal(km);

		// THEN check that current locals collection does not have the removed local knowledge manager 
		assertEquals(false, kmRegistry.getLocals().contains(km));
	
	}
	
	@Test 
	public void testShadowKnowledgeManagerIsRemoved(){
		
		// WHEN call removeShadow(KnowledgeManager km)
		Collection<KnowledgeManager> colShadowKM = new LinkedList<KnowledgeManager>();
		when(kmRegistry.getShadows()).thenReturn(colShadowKM);
		kmRegistry.removeShadow(km);

		// THEN check that current shadows collection does not have the removed shadow knowledge manager 
		assertEquals(false, kmRegistry.getShadows().contains(km));
	
	}
	
	@Test 
	public void testGetLocals(){
		Collection<KnowledgeManager> colLocals = new LinkedList<KnowledgeManager>();
		when(kmRegistry.getLocals()).thenReturn(colLocals);

		// WHEN call the getLocals()
		// THEN a collection of all registered local knowledgeManagers is returned
		assertEquals(colLocals , kmRegistry.getLocals());
	}	
	
	@Test 
	public void testGetShadows(){
		Collection<KnowledgeManager> colShadows = new LinkedList<KnowledgeManager>();
		when(kmRegistry.getShadows()).thenReturn(colShadows);
		
		// WHEN call the getShadows()
		// THEN a collection of all registered shadow knowledgeManagers is returned
		assertEquals(colShadows , kmRegistry.getShadows());
	}	
 	
}
