package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * This container allows to deal with both local and replica knowledge managers by implementing both interfaces: {@link LocalKnowledgeManagerContainer} 
 * and  {@link ReplicaKnowledgeManagerContainer}. It retrieves all available locals and replicas knowledge managers in the container. 
 * In addition, it gives the ability to register a new local listeners to listen for events caused by local knowledge managers changes, and 
 * register a new replica listener to listen for events caused by replica knowledge managers changes.
 * By registered listeners, the container have the ability to listen to events related to adding or removing both kinds of knowledge managers (local and replica).
 * Also, the container acts as a factory for locals/replicas Knowledge managers and register to them all the existing local/replica listeners in the container.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class CloningKnowledgeManagerContainer extends LocalKnowledgeManagerContainer implements
		ReplicaKnowledgeManagerContainer {

	protected final List<KnowledgeManager> replicas;
	protected final List<ReplicaListener> replicaListeners;

	public CloningKnowledgeManagerContainer() {
		this.replicas = new LinkedList<>();
		this.replicaListeners = new LinkedList<>();
	}
	
	public KnowledgeManager createLocal(String id) {
		return super.createLocal(id);
	}

	@Override
	public KnowledgeManager removeReplica(KnowledgeManager km) {
		KnowledgeManager kmVar = null;
		if (replicas.contains(km)) {
			replicas.remove(km);
			for (ReplicaListener listener : replicaListeners){
				listener.replicaRemoved(km, this);
			}
			kmVar = km;
		}
		return kmVar;
	}

	@Override
	public List<KnowledgeManager> getReplicas() {
		return replicas;
	}

	@Override
	public void registerReplicaListener(ReplicaListener listener) {
		if (!replicaListeners.contains(listener)){
			replicaListeners.add(listener);
		}
	}

	@Override
	public KnowledgeManager createReplica(String id) {
		KnowledgeManager kmVar = null;
		KnowledgeManager result = new CloningKnowledgeManager(id);
		if (!(locals.contains(result) || replicas.contains(result))) {
			replicas.add(result);
			for (ReplicaListener listener : replicaListeners){
				listener.replicaCreated(result, this);
			}
			kmVar = result;
		}
		return kmVar;
	}

}
