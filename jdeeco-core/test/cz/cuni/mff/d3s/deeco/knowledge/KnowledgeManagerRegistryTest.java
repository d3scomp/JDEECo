package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * KnowledgeManagerRegistry testing. The test checks the correctness of
 * adding/removing/getting the KnowledgeManagers of locals and shadows.
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

	private KnowledgeManagerContainer kmContainer;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.km = mock(KnowledgeManager.class);
		this.kmContainer = mock(KnowledgeManagerContainer.class);
	}

	// FIXME TB: The tests below need to be fixed to comply to BDD.
	@Ignore
	@Test
	public void testCreateLocal() {

		// WHEN create a new local knowledge manager
		// THEN define the behavior of the KnowledgeManagerRegistry:
		// the value "newKM" returned by createLocal(Task localTask)
		when(kmContainer.createLocal()).thenReturn(km);

		// WHEN call the createLocal
		km = kmContainer.createLocal();
		// THEN a new knowledge manager is added to the locals and returned
		assertNotNull(km);
		assertEquals(true, km instanceof KnowledgeManager);
	}

	@Ignore
	@Test
	public void testCreateShadow() {
		// WHEN create a new remote knowledge manager
		// THEN define the behavior of the KnowledgeManagerRegistry:
		// the value "newKM" returned by createShadow(Task remoteTask)
		// when(kmRegistry.createShadow()).thenReturn(km);

		// WHEN call the createShadow
		// km = kmRegistry.createShadow();
		// THEN check if a new knowledge manager is returned and not null
		assertNotNull(km);
		assertEquals(true, km instanceof KnowledgeManager);

	}

	@Ignore
	@Test
	public void testRemoveLocal() {
		// WHEN define a Collection of local knowledge managers that do not have
		// km
		List<KnowledgeManager> colLocalKM = new LinkedList<KnowledgeManager>();
		// THEN define the behavior of the KnowledgeManagerRegistry:
		// 1- return "colLocalKM" by calling getLocals()
		when(kmContainer.getLocals()).thenReturn(colLocalKM);

		// WHEN call removeLocal(KnowledgeManager km)
		kmContainer.removeLocal(km);
		// THEN check current locals collection does not have the km
		assertEquals(false, kmContainer.getLocals().contains(km));
	}

	@Ignore
	@Test
	public void testRemoveShadow() {
		// WHEN define a Collection of shadow knowledge managers that do not
		// have km
		// Collection<KnowledgeManager> colShadowKM = new
		// LinkedList<KnowledgeManager>();
		// THEN define the behavior of the KnowledgeManagerRegistry:
		// 1- return "colShadowKM" by calling getShadows()
		// when(.getShadows()).thenReturn(colShadowKM);

		// WHEN call removeShadow(KnowledgeManager km)
		// kmRegistry.removeShadow(km);
		// THEN check current shadows collection does not have the km
		// assertEquals(false, kmRegistry.getShadows().contains(km));
	}

	@Ignore
	@Test
	public void testGetLocals() {
		// WHEN define a collection of locals
		// Collection<KnowledgeManager> colLocals = new
		// LinkedList<KnowledgeManager>();
		// THEN define the behavior of the KnowledgeManagerRegistry:
		// 1- return "colLocals" by calling getLocals()
		// when(kmRegistry.getLocals()).thenReturn(colLocals);

		// WHEN call the getLocals()
		// THEN a collection of all registered local knowledgeManagers is
		// returned
		// assertEquals(colLocals , kmRegistry.getLocals());
	}

	@Ignore
	@Test
	public void testGetShadows() {
		// WHEN define a collection of shadows
		// Collection<KnowledgeManager> colShadows = new
		// LinkedList<KnowledgeManager>();
		// THEN define the behavior of the KnowledgeManagerRegistry:
		// 1- return "colShadows" by calling getShadows()
		// when(kmRegistry.getShadows()).thenReturn(colShadows);

		// WHEN call the getShadows()
		// THEN a collection of all registered shadow knowledgeManagers is
		// returned
		// assertEquals(colShadows , kmRegistry.getShadows());
	}

}
