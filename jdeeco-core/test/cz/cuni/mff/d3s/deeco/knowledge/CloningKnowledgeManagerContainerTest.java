package cz.cuni.mff.d3s.deeco.knowledge;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import static org.mockito.Matchers.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
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
		verify(localListener).localCreated(local);
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
		verify(replicaListener).replicaCreated(replica);
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
		verify(localListener).localRemoved(local);
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
		KnowledgeManager replica = tested.createReplicaFor(any(KnowledgeManager.class));
		tested.removeReplica(replica);
		// THEN the listener is notified about this fact
		verify(replicaListener).replicaRemoved(replica);
		// and none of the local listeners is notified
		verifyZeroInteractions(localListener);
	}
}
