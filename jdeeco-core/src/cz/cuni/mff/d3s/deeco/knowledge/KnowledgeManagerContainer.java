package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * 
 * This container allows to deal with both local and replica knowledge managers
 * by implementing both interfaces: {@link LocalKnowledgeManagerContainer} and
 * {@link ReplicaKnowledgeManagerContainer}. It retrieves all available locals
 * and replicas knowledge managers in the container. In addition, it gives the
 * ability to register a new local listeners to listen for events caused by
 * local knowledge managers changes, and register a new replica listener to
 * listen for events caused by replica knowledge managers changes. By registered
 * listeners, the container have the ability to listen to events related to
 * adding or removing both kinds of knowledge managers (local and replica).
 * Also, the container acts as a factory for locals/replicas Knowledge managers
 * and register to them all the existing local/replica listeners in the
 * container.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class KnowledgeManagerContainer implements KnowledgeDataReceiver,
		KnowledgeDataProvider {

	protected final List<KnowledgeManager> replicas;
	protected final List<ReplicaListener> replicaListeners;
	protected final List<KnowledgeManager> locals;
	protected final List<LocalListener> localListeners;

	private final List<KnowledgePath> emptyPath;

	public KnowledgeManagerContainer() {
		this.replicas = new LinkedList<>();
		this.replicaListeners = new LinkedList<>();
		this.locals = new LinkedList<>();
		this.localListeners = new LinkedList<>();

		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		KnowledgePath empty = factory.createKnowledgePath();
		emptyPath = new LinkedList<>();
		emptyPath.add(empty);
	}

	/**
	 * Creates a new instance of local knowledge manager with the specified id,
	 * add it to the container and register all existing local listener to it.
	 * 
	 * @param String
	 *            the identifier of the knowledge manager
	 * @return {@link KnowledgeManager} the newly created object containing
	 *         values for the specified knowledge paths.
	 */
	public KnowledgeManager createLocal(String id) {
		KnowledgeManager result = new CloningKnowledgeManager(id);
		locals.add(result);
		for (LocalListener listener : localListeners) {
			listener.localCreated(result, this);
		}
		return result;
	}

	/**
	 * Removes a local knowledge manager from the container and return it. This
	 * implies also informing the local listener about removing this knowledge
	 * manager.
	 * 
	 * @param {@link KnowledgeManager} local knowledge manager to be removed
	 * @return {@link KnowledgeManager} the removed local knowledge manager
	 *         object containing values for the specified knowledge paths
	 */
	public KnowledgeManager removeLocal(KnowledgeManager km) {
		KnowledgeManager kmVar = null;
		if (locals.contains(km)) {
			locals.remove(km);
			for (LocalListener listener : localListeners) {
				listener.localRemoved(km, this);
			}
			kmVar = km;
		}
		return kmVar;
	}

	/**
	 * Retrieves all the local knowledge managers in the container.
	 * 
	 * @return List<{@link KnowledgeManager}> object containing values for the
	 *         specified knowledge paths
	 */
	public List<KnowledgeManager> getLocals() {
		return locals;
	}

	/**
	 * Adds the local listener to the container
	 * 
	 * @param {@link LocalListener} listens for adding local knowledge managers
	 *        to the container or removing them.
	 */
	public void registerLocalListener(LocalListener listener) {
		if (!localListeners.contains(listener)) {
			localListeners.add(listener);
		}
	}

	/**
	 * Removes a replica knowledge manager from the container and return it.
	 * This implies also informing the replica listener about removing this
	 * knowledge manager.
	 * 
	 * @param {@link KnowledgeManager} replica knowledge manager to be removed
	 * @return {@link KnowledgeManager} the removed replica knowledge manager
	 *         object containing values for the specified knowledge paths
	 */
	public KnowledgeManager removeReplica(KnowledgeManager km) {
		KnowledgeManager kmVar = null;
		if (replicas.contains(km)) {
			replicas.remove(km);
			for (ReplicaListener listener : replicaListeners) {
				listener.replicaUnregistered(km, this);
			}
			kmVar = km;
		}
		return kmVar;
	}

	/**
	 * Retrieves all the replica knowledge managers in the container.
	 * 
	 * @return List<{@link KnowledgeManager}> object containing values for the
	 *         specified knowledge paths
	 */
	public List<KnowledgeManager> getReplicas() {
		return replicas;
	}

	/**
	 * Adds the replica listener to the container
	 * 
	 * @param {@link ReplicaListener} listens for adding replica knowledge
	 *        managers to the container or removing them.
	 */
	public void registerReplicaListener(ReplicaListener listener) {
		if (!replicaListeners.contains(listener)) {
			replicaListeners.add(listener);
		}
	}

	/**
	 * Adds replica knowledge manager to the container and registers all
	 * existing replica listener to it.
	 * 
	 * @param id
	 *            of the replica being registered
	 */
	protected KnowledgeManager createReplica(String id) {
		KnowledgeManager result = new CloningKnowledgeManager(id);
		if (!replicas.contains(result)) {
			replicas.add(result);
			for (ReplicaListener listener : replicaListeners) {
				listener.replicaRegistered(result, this);
			}
			return result;
		} else
			return replicas.get(replicas.indexOf(result));
	}

	@Override
	public void receive(List<? extends KnowledgeData> knowledgeData) {
		if (knowledgeData != null) {
			KnowledgeManager km;
			for (KnowledgeData kd : knowledgeData) {
				km = createReplica(kd.getComponentId());
				try {
					km.update(toChangeSet(kd.getKnowledge()));
				} catch (KnowledgeUpdateException e) {
					Log.e("KnowledgeManagerContainer error", e);
				}
			}
		}
	}

	@Override
	public List<? extends KnowledgeData> getKnowledgeData() {
		List<KnowledgeData> result = new LinkedList<>();
		for (KnowledgeManager km : getLocals())
			try {
				result.add(new KnowledgeData(km.getId(), km.get(emptyPath)));
			} catch (KnowledgeNotFoundException e) {
				Log.e("KnowledgeManagerContainer error", e);
			}
		for (KnowledgeManager km : getReplicas())
			try {
				result.add(new KnowledgeData(km.getId(), km.get(emptyPath)));
			} catch (KnowledgeNotFoundException e) {
				Log.e("KnowledgeManagerContainer error", e);
			}
		return result;
	}

	private ChangeSet toChangeSet(ValueSet valueSet) {
		if (valueSet != null) {
			ChangeSet result = new ChangeSet();
			for (KnowledgePath kp : valueSet.getKnowledgePaths())
				result.setValue(kp, valueSet.getValue(kp));
			return result;
		} else {
			return null;
		}
	}

}
