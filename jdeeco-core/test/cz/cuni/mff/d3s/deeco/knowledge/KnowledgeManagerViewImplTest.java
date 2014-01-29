package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;

/**
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class KnowledgeManagerViewImplTest {
	
	@Mock
	private KnowledgeManagerContainer container;
	@Mock
	private KnowledgeManager knowledgeManager;
	@Mock
	private KnowledgeManager local;
	@Mock
	private KnowledgeManager replica;
	@Mock
	private ShadowsTriggerListener listener;
	@Mock
	private KnowledgeChangeTrigger trigger;
	
	private ShadowKnowledgeManagerRegistryImpl tested;
	
	@Before
	public void setUp() {
		initMocks(this);
		tested = new ShadowKnowledgeManagerRegistryImpl(knowledgeManager, container);
	}
	
	@Test
	public void getOtherKnowledgeManagersTest() {
		// WHEN the container already consists of some local and replica
		// knowledge managers
		List<KnowledgeManager> locals = new LinkedList<>();
		locals.add(local);
		locals.add(knowledgeManager);
		List<KnowledgeManager> replicas = new LinkedList<>();
		replicas.add(replica);
		when(container.getLocals()).thenReturn(locals);
		when(container.getReplicas()).thenReturn(replicas);

		// and WHEN the 'getOtherKnowledgeManagers' is called on the
		// KnowledgeManagerView instance

		Collection<ReadOnlyKnowledgeManager> result = tested.getShadowKnowledgeManagers();
		
		// THEN the container is accessed for local and replica knowledge
		// managers
		
		verify(container).getLocals();
		verify(container).getReplicas();
		
		// and THEN the KnowledgeManagerView instance returns all container's
		// knowledge managers apart of the one referenced by the
		// KnowledgeManagerView instance
		
		assertTrue(result.size() == 2);
		List<KnowledgeManager> expectedResult = new LinkedList<>();
		expectedResult.addAll(locals);
		expectedResult.addAll(replicas);
		expectedResult.remove(knowledgeManager);
		assertEquals(expectedResult, result);
		
	}

	@Test
	public void registerTest() {
		// WHEN the container already consists of some local and replica
		// knowledge managers
		
		List<KnowledgeManager> locals = new LinkedList<>();
		locals.add(knowledgeManager);
		List<KnowledgeManager> replicas = new LinkedList<>();
		replicas.add(replica);
		when(container.getLocals()).thenReturn(locals);
		when(container.getReplicas()).thenReturn(replicas);

		// and WHEN the 'register' is called on the
		// KnowledgeManagerView instance
		
		tested.register(trigger, listener);

		// THEN each of the KnowledgeManager instance hosted by the container is
		// registered with the trigger

		verify(knowledgeManager).register(eq(trigger), any(TriggerListener.class));
		verify(replica).register(eq(trigger), any(TriggerListener.class));		
	}

	@Test
	public void unregisterTest() {
		// WHEN the container already consists of some local and replica
		// knowledge managers
		List<KnowledgeManager> locals = new LinkedList<>();
		locals.add(knowledgeManager);
		List<KnowledgeManager> replicas = new LinkedList<>();
		replicas.add(replica);
		when(container.getLocals()).thenReturn(locals);
		when(container.getReplicas()).thenReturn(replicas);

		// and WHEN the 'register' is called on the
		// KnowledgeManagerView instance
		
		tested.register(trigger, listener);
		
		// and WHEN the 'unregister' is called on the
		// KnowledgeManagerView instance

		tested.unregister(trigger, listener);
		
		// THEN each of the KnowledgeManager instance hosted by the container is
		// unregistered with the respective trigger

		verify(knowledgeManager).unregister(eq(trigger), any(TriggerListener.class));
		verify(replica).unregister(eq(trigger), any(TriggerListener.class));
		
		// and any other knowledge manager added later is not registered with
		// the trigger
		
		tested.localCreated(local, container);
		verify(local, never()).register(eq(trigger), any(TriggerListener.class));

	}

	@Test
	public void localCreatedTest() {
		// WHEN the container has created a new local knowledge manager
		// and WHEN the KnowledgeManagerView instance is notified about this
		
		tested.register(trigger, listener);
		tested.localCreated(local, container);

		// THEN all memorised triggers are registered with the new knowledge
		// manager
		
		verify(local).register(eq(trigger), any(TriggerListener.class));
	}

	@Test
	public void replicaCreatedTest() {
		// WHEN the container has created a new replica knowledge manager
		// and WHEN the KnowledgeManagerView instance is notified about this

		tested.register(trigger, listener);
		tested.replicaRegistered(replica, container);
		
		// and all memorised triggers are registered with the new knowledge
		// manager
		
		verify(replica).register(eq(trigger), any(TriggerListener.class));
	}

	@Test
	public void localRemovedTest() {
		// WHEN the container has removed a local knowledge manager
		// and WHEN the KnowledgeManagerView instance is notified about this
		List<KnowledgeManager> locals = new LinkedList<>();
		locals.add(local);
		locals.add(knowledgeManager);
		when(container.getLocals()).thenReturn(locals);
		
		tested.register(trigger, listener);
		tested.localRemoved(local, container);
		
		// THEN all triggers registered by the KnowledgeManagerView instance are
		// removed from the knowledge manager being removed
		
		verify(local).unregister(eq(trigger), any(TriggerListener.class));
	}

	@Test
	public void replicaRemovedTest() {
		// WHEN the container has removed a replica knowledge manager
		// and WHEN the KnowledgeManagerView instance is notified about this
		List<KnowledgeManager> replicas = new LinkedList<>();
		replicas.add(replica);
		when(container.getReplicas()).thenReturn(replicas);
		
		
		tested.register(trigger, listener);
		tested.localRemoved(replica, container);
		
		// THEN all triggers registered by the KnowledgeManagerView instance are
		// removed from the knowledge manager being removed
		
		verify(replica).unregister(eq(trigger), any(TriggerListener.class));
	}
}
