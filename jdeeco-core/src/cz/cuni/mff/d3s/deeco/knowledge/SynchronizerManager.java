package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class SynchronizerManager implements LocalListener {

	private final List<LocalKnowledgeManagerContainer> localsContainers;
	private final List<ReplicaKnowledgeManagerContainer> replicasContainers;
	private final List<Synchronizer> synchronizers;

	public SynchronizerManager() {
		this.localsContainers = new LinkedList<>();
		this.replicasContainers = new LinkedList<>();
		this.synchronizers = new LinkedList<>();
	}

	@Override
	public void localCreated(KnowledgeManager km,
			LocalKnowledgeManagerContainer container) {
		replicateLocal(km, replicasContainers);
	}

	@Override
	public void localRemoved(KnowledgeManager km,
			LocalKnowledgeManagerContainer container) {
		// Remove relevant replicas and synchronisers
		for (ReplicaKnowledgeManagerContainer rContainer : replicasContainers)
			removeReplicaAndSynchronizerFor(km, rContainer);
	}

	public void registerReplicasContainer(
			ReplicaKnowledgeManagerContainer container) {
		if (replicasContainers.contains(container))
			return;
		// Add the container to the list
		replicasContainers.add(container);
		List<ReplicaKnowledgeManagerContainer> toReplicateIn = new LinkedList<>();
		toReplicateIn.add(container);
		// Create a replica for each local knowledge manager at each local
		// container already registered
		for (LocalKnowledgeManagerContainer localKMContainer : localsContainers) {
			for (KnowledgeManager local : localKMContainer.getLocals())
				replicateLocal(local, toReplicateIn);
		}
	}

	public void registerLocalsContainer(LocalKnowledgeManagerContainer container) {
		if (localsContainers.contains(container))
			return;
		// Add the container
		localsContainers.add(container);
		// Create relevant replicas for each local knowledge manager provided by
		// the new local container
		for (KnowledgeManager local : container.getLocals())
			localCreated(local, container);
	}

	public void unregisterReplicasContainer(
			ReplicaKnowledgeManagerContainer container) {
		// Remove container from the list
		if (replicasContainers.contains(container))
			replicasContainers.remove(container);
		// Clear the replicas from the container being removed
		// and remove relevant syncs
		for (LocalKnowledgeManagerContainer lContainer : localsContainers) {
			for (KnowledgeManager local : lContainer.getLocals()) {
				removeReplicaAndSynchronizerFor(local, container);
			}
		}
	}

	public void unregisterLocalsContainer(
			LocalKnowledgeManagerContainer container) {
		if (localsContainers.contains(container))
			localsContainers.remove(container);
		// Remove replicas created for this local knowledge managers
		// provider and remove relevant synchronisers
		for (KnowledgeManager local : container.getLocals()) {
			for (ReplicaKnowledgeManagerContainer rContainer : replicasContainers) {
				removeReplicaAndSynchronizerFor(local, rContainer);
			}
		}
	}

	private void removeReplicaAndSynchronizerFor(KnowledgeManager local,
			ReplicaKnowledgeManagerContainer fromContainer) {
		int index;
		Synchronizer synchronizer;
		KnowledgeManager replica = fromContainer.removeReplica(local);
		if (replica != null) {
			index = synchronizers.indexOf(new Synchronizer(local, replica));
			if (index > -1) {
				synchronizer = synchronizers.remove(index);
				synchronizer.unregister();
			}
		}
	}

	private void replicateLocal(KnowledgeManager local,
			List<ReplicaKnowledgeManagerContainer> toReplicateIn) {
		KnowledgeManager replica;
		for (ReplicaKnowledgeManagerContainer rContainer : toReplicateIn) {
			replica = rContainer.createReplica(local.getId());
			if (replica != null)
				synchronizers.add(new Synchronizer(local, replica));
		}
	}
}
