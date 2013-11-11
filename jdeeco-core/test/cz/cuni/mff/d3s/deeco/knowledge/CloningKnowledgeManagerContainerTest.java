package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class CloningKnowledgeManagerContainerTest {

	@Mock
	private LocalListener localListener;
	@Mock
	private ReplicaListener replicaListener;

	private CloningKnowledgeManagerContainer tested;

	@Before
	public void setUp() {
		initMocks(this);
		this.tested = new CloningKnowledgeManagerContainer();
	}

	@Test
	public void notifyLocalCreatedTest() {
		// WHEN a LocalListener is registered at the container
		// and WHEN a ReplicaListener is registered at the container
		tested.registerLocalListener(localListener);
		tested.registerReplicaListener(replicaListener);
		// and WHEN new local knowledge manager has been created
		KnowledgeManager local = tested.createLocal();
		// THEN the listener is notified about this fact
		verify(localListener).localCreated(local,
				any(LocalKnowledgeManagerContainer.class));
		// and none of the replica listeners is notified
		verifyZeroInteractions(replicaListener);
	}

	@Test
	public void notifyReplicaCreatedTest() {
		// WHEN a ReplicaListener is registered at the container
		// and WHEN a LocalListener is registered at the container
		tested.registerReplicaListener(replicaListener);
		tested.registerLocalListener(localListener);
		// and WHEN new replica knowledge manager has been created
		KnowledgeManager local = mock(KnowledgeManager.class);
		KnowledgeManager replica = tested.createReplicaFor(local);
		// THEN the listener is notified about this fact
		verify(replicaListener).replicaCreated(replica,
				any(ReplicaKnowledgeManagerContainer.class));
		// and none of the local listeners is notified
		verifyZeroInteractions(localListener);
	}

	@Test
	public void notifyLocalRemovedTest() {
		// WHEN a LocalListener is registered at the container
		// and WHEN a ReplicaListener is registered at the container
		tested.registerLocalListener(localListener);
		tested.registerReplicaListener(replicaListener);
		// and WHEN a local knowledge manager has been removed
		KnowledgeManager local = tested.createLocal();
		tested.removeLocal(local);
		// THEN the listener is notified about this fact
		verify(localListener).localRemoved(local,
				any(LocalKnowledgeManagerContainer.class));
		// and none of the replica listeners is notified
		verifyZeroInteractions(replicaListener);
	}

	@Test
	public void notifyReplicaRemovedTest() {
		// WHEN a ReplicaListener is registered at the container
		// and WHEN a LocalListener is registered at the container
		tested.registerReplicaListener(replicaListener);
		tested.registerLocalListener(localListener);
		// and WHEN a replica of a knowledge manager has been removed
		KnowledgeManager replica = tested
				.createReplicaFor(any(KnowledgeManager.class));
		tested.removeReplica(replica);
		// THEN the listener is notified about this fact
		verify(replicaListener).replicaRemoved(replica,
				any(ReplicaKnowledgeManagerContainer.class));
		// and none of the local listeners is notified
		verifyZeroInteractions(localListener);
	}

	@Test
	public void getLocalsTest() {
		// WHEN a set of local knowledge managers has been created within the
		// container instance
		List<KnowledgeManager> locals = new LinkedList<>();
		locals.add(tested.createLocal());
		locals.add(tested.createLocal());
		locals.add(tested.createLocal());
		// WHEN the container is accessed for all local knowledge managers
		List<KnowledgeManager> containerLocals = tested.getLocals();
		// THEN the container returns all local knowledge managers created
		// before
		assertEquals(locals, containerLocals);
	}

	@Test
	public void getReplicasTest() {
		// WHEN a set of replica knowledge managers has been created within the
		// container instance
		List<KnowledgeManager> replicas = new LinkedList<>();
		KnowledgeManager km = mock(KnowledgeManager.class);
		replicas.add(tested.createReplicaFor(km));
		km = mock(KnowledgeManager.class);
		replicas.add(tested.createReplicaFor(km));
		km = mock(KnowledgeManager.class);
		replicas.add(tested.createReplicaFor(km));
		// WHEN the container is accessed for all replica knowledge managers
		List<KnowledgeManager> containerReplicas = tested.getReplicas();
		// THEN the container returns all replica knowledge managers created
		// before
		assertEquals(replicas, containerReplicas);
	}
}
