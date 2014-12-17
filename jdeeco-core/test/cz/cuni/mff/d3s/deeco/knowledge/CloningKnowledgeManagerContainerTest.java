package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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
		ComponentInstance component = RuntimeMetadataFactoryExt.eINSTANCE.createComponentInstance();
		component.setName("C1");
		this.runtimeMetaData.getComponentInstances().add(component);
		
		this.tested = new KnowledgeManagerContainer(new CloningKnowledgeManagerFactory(), runtimeMetaData); 
	}

	@Test
	public void notifyLocalCreatedTest() {
		// WHEN a LocalListener is registered at the container
		// and WHEN a ReplicaListener is registered at the container
		tested.registerLocalListener(localListener);
		tested.registerReplicaListener(replicaListener);
		// and WHEN new local knowledge manager has been created
		KnowledgeManager local = tested.createLocal("L", null);
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
		Collection<KnowledgeManager> replicas = tested.createReplica("T1");
		// THEN the listener is notified about this fact
		for (KnowledgeManager replica : replicas) {
			verify(replicaListener).replicaRegistered(replica, tested);
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
		KnowledgeManager local = tested.createLocal("L", null);
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
		locals.add(l1 = tested.createLocal("L1", null));
		locals.add(l2 = tested.createLocal("L2", null));
		locals.add(l3 = tested.createLocal("L3", null));
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

		r1 = tested.createReplica("R1");
		r2 = tested.createReplica("R2");
		r3 = tested.createReplica("R3");
				
		// WHEN the container is accessed for all replica knowledge managers
		Collection<KnowledgeManager> containerReplicas = tested.getReplicas();
		
		// THEN the container returns all replica knowledge managers created
		// before
		assertEquals(3, containerReplicas.size());
		r1.stream().forEach(r ->  assertThat(containerReplicas, hasItem(r)));
		r2.stream().forEach(r ->  assertThat(containerReplicas, hasItem(r)));
		r3.stream().forEach(r ->  assertThat(containerReplicas, hasItem(r)));
	}
}
