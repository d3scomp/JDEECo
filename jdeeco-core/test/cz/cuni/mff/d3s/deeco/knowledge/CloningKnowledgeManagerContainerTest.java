package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;

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

	private KnowledgeManagerContainer tested;
	
	private RuntimeMetadata runtimeMetaData;
	
	@Before
	public void setUp() {
		initMocks(this);
		
		// create runtime with a single component
		this.runtimeMetaData = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		
		KnowledgeManager mockKnowledgeManager = Mockito.mock(KnowledgeManager.class);
		Mockito.when(mockKnowledgeManager.getRoles()).thenReturn(null);
		
		ComponentInstance component1 = RuntimeMetadataFactoryExt.eINSTANCE.createComponentInstance();
		component1.setName("C1");
		component1.setKnowledgeManager(mockKnowledgeManager);
		this.runtimeMetaData.getComponentInstances().add(component1);
		
		ComponentInstance component2 = RuntimeMetadataFactoryExt.eINSTANCE.createComponentInstance();
		component2.setName("C2");
		component2.setKnowledgeManager(mockKnowledgeManager);
		this.runtimeMetaData.getComponentInstances().add(component2);
		
		this.tested = new KnowledgeManagerContainer(new CloningKnowledgeManagerFactory(), runtimeMetaData); 
	}

	@Test
	public void notifyLocalCreatedTest() {
		// WHEN a LocalListener is registered at the container
		// and WHEN a ReplicaListener is registered at the container
		tested.registerLocalListener(localListener);
		tested.registerReplicaListener(replicaListener);
		// and WHEN new local knowledge manager has been created
		KnowledgeManager local = tested.createLocal("L", null, null);
		// THEN the listener is notified about this fact
		verify(localListener).localCreated(local, tested);
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
		Collection<KnowledgeManager> replicas = tested.createReplica("T1", null);
		// THEN the listener is notified about this fact
		for (KnowledgeManager replica : replicas) {
			verify(replicaListener, new Times(2)).replicaRegistered(replica, tested);
		}
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
		KnowledgeManager local = tested.createLocal("L", null, null);
		tested.removeLocal(local);
		// THEN the listener is notified about this fact
		verify(localListener).localRemoved(local, tested);
		// and none of the replica listeners is notified
		verifyZeroInteractions(replicaListener);
	}

	@Test
	public void getLocalsTest() {
		// WHEN a set of local knowledge managers has been created within the
		// container instance
		Collection<KnowledgeManager> locals = new LinkedList<>();
		KnowledgeManager l1, l2, l3;
		locals.add(l1 = tested.createLocal("L1", null, null));
		locals.add(l2 = tested.createLocal("L2", null, null));
		locals.add(l3 = tested.createLocal("L3", null, null));
		// WHEN the container is accessed for all local knowledge managers
		Collection<KnowledgeManager> containerLocals = tested.getLocals();
		// THEN the container returns all local knowledge managers created
		// before
		assertEquals(3, containerLocals.size());
		assertThat(containerLocals, hasItem(l1));
		assertThat(containerLocals, hasItem(l2));
		assertThat(containerLocals, hasItem(l3));
	}

	@Test
	public void getReplicasTest() {
		// WHEN a set of replica knowledge managers has been created within the
		// container instance
		
		Collection<KnowledgeManager> r1, r2, r3;

		r1 = tested.createReplica("R1", null);
		r2 = tested.createReplica("R2", null);
		r3 = tested.createReplica("R3", null);
				
		// WHEN the container is accessed for all replica knowledge managers
		Collection<KnowledgeManager> containerReplicas = tested.getReplicas();
		
		// THEN the container returns all replica knowledge managers created
		// before (replica for each registered component)
		assertEquals(3*2, containerReplicas.size());
		checkReplicas(r1, containerReplicas);
		checkReplicas(r2, containerReplicas);
		checkReplicas(r3, containerReplicas);
	}

	private void checkReplicas(Collection<KnowledgeManager> replicas, Collection<KnowledgeManager> containerReplicas) {
		replicas.stream().forEach(r ->  assertThat(containerReplicas, hasItem(r)));
		List<KnowledgeManager> sortedReplicas = replicas.stream().sorted((c1, c2) -> c1.getComponent().getName().compareTo(c2.getComponent().getName())).collect(Collectors.toList());
		assertEquals("C1", sortedReplicas.get(0).getComponent().getName());
		assertEquals("C2", sortedReplicas.get(1).getComponent().getName());
		
	}
	
	
}
