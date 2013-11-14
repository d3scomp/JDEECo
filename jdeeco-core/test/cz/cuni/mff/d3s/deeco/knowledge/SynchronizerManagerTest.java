package cz.cuni.mff.d3s.deeco.knowledge;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class SynchronizerManagerTest {

	@Mock
	private LocalKnowledgeManagerContainer localsContainer;

	@Mock
	private ReplicaKnowledgeManagerContainer replicasContainer;

	@Mock
	private KnowledgeManager local;

	@Mock
	private KnowledgeManager replica;

	private SynchronizerManager tested;

	@Before
	public void setUp() {
		initMocks(this);
		this.tested = new SynchronizerManager();
		List<KnowledgeManager> locals = new LinkedList<>();
		locals.add(local);
		List<KnowledgeManager> replicas = new LinkedList<>();
		replicas.add(replica);

		when(local.getId()).thenReturn("TEST");
		when(localsContainer.getLocals()).thenReturn(locals);
		when(replicasContainer.getReplicas()).thenReturn(replicas);
	}

	@Test
	public void registerLocalKMContainerTest() {
		// WHEN a ReplicaKnowledgeManagerContainer has been previously
		// registered with the synchroniser manager
		tested.registerReplicasContainer(replicasContainer);
		// and WHEN a LocalKnowledgeManagerContainer is being registered with
		// the synchroniser manager
		when(replicasContainer.createReplica(local.getId())).thenReturn(replica);
		tested.registerLocalsContainer(localsContainer);
		// THEN all local knowledge managers (provided by the
		// LocalKnowledgeManagerContainer instances) are replicated
		verify(replicasContainer).createReplica(local.getId());
		// and THEN a Synchronizer instance is registered with the local
		// knowledge manager
		verify(local).register(any(KnowledgeChangeTrigger.class),
				any(Synchronizer.class));
	}

	@Test
	public void registerReplicaKMContainerTest() {
		// WHEN a LocalKnowledgeManagerContainer has been previously registered
		// with the synchroniser manager
		tested.registerLocalsContainer(localsContainer);
		// and WHEN a ReplicaKnowledgeManagerContainer is being registered with
		// the synchroniser manager
		when(replicasContainer.createReplica(local.getId())).thenReturn(replica);
		tested.registerReplicasContainer(replicasContainer);
		// THEN all local knowledge managers (provided by all
		// LocalKnowledgeManagerContainer instances registered with the
		// synchroniser manager) are replicated
		verify(replicasContainer).createReplica(local.getId());
		// and THEN a Synchronizer instance is registered with local
		// knowledge managers
		verify(local).register(any(KnowledgeChangeTrigger.class),
				any(Synchronizer.class));
	}

	@Test
	public void unregisterLocalKMContainerTest() {
		// WHEN a ReplicaKnowledgeManagerContainer has been previously
		// registered with the synchroniser manager
		tested.registerReplicasContainer(replicasContainer);
		// and WHEN a LocalKnowledgeManagerContainer has also been registered
		// with the synchroniser manager
		when(replicasContainer.createReplica(local.getId())).thenReturn(replica);
		tested.registerLocalsContainer(localsContainer);
		// and WHEN the LocalKnowledgeManagerContainer is being unregistered
		// from the synchroniser manager after a while
		when(replicasContainer.removeReplica(local)).thenReturn(replica);
		tested.unregisterLocalsContainer(localsContainer);
		// THEN all replicas of all local knowledge managers provided by the
		// container being removed should be removed together with the relevant
		// synchroniser instances
		verify(replicasContainer).removeReplica(local);
		verify(local).unregister(any(KnowledgeChangeTrigger.class),
				any(Synchronizer.class));
	}

	@Test
	public void unregisterReplicaKMContainerTest() {
		// WHEN a ReplicaKnowledgeManagerContainer has been previously
		// registered with the synchroniser manager
		tested.registerReplicasContainer(replicasContainer);
		// and WHEN a LocalKnowledgeManagerContainer has also been registered
		// with the synchroniser manager
		when(replicasContainer.createReplica(local.getId())).thenReturn(replica);
		tested.registerLocalsContainer(localsContainer);
		// and WHEN the ReplicaKnowledgeManagerContainer is being unregistered
		// from the synchroniser manager after a while
		when(replicasContainer.removeReplica(local)).thenReturn(replica);
		tested.unregisterLocalsContainer(localsContainer);
		// THEN all replicas of all the replicas (of the container being
		// removed) of local knowledge managers provided by local knowledge
		// manager containers are removed together with respective synchroniser
		// instances
		verify(replicasContainer).removeReplica(local);
		verify(local).unregister(any(KnowledgeChangeTrigger.class),
				any(Synchronizer.class));
	}
}
