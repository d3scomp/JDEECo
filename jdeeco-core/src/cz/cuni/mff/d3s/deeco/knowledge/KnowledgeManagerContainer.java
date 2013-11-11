package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public abstract class KnowledgeManagerContainer implements
		ReplicaKnowledgeManagerContainer, LocalKnowledgeManagerContainer {

	protected final List<KnowledgeManager> locals;
	protected final List<KnowledgeManager> replicas;
	protected final List<LocalListener> localListeners;
	protected final List<ReplicaListener> replicaListeners;
	
	public KnowledgeManagerContainer() {
		this.locals = new LinkedList<>();
		this.replicas = new LinkedList<>();
		this.localListeners = new LinkedList<>();
		this.replicaListeners = new LinkedList<>();
	}
	
	@Override
	public synchronized KnowledgeManager createLocal() {
		KnowledgeManager result = new CloningKnowledgeManager();
		locals.add(result);
		for (LocalListener listener : localListeners)
			listener.localCreated(result, this);
		return result;
	}

	@Override
	public synchronized KnowledgeManager removeLocal(KnowledgeManager km) {
		if (locals.contains(km)) {
			locals.remove(km);
			for (LocalListener listener : localListeners)
				listener.localRemoved(km, this);
			return km;
		}
		return null;
	}

	@Override
	public synchronized List<KnowledgeManager> getLocals() {
		return locals;
	}

	@Override
	public synchronized void registerLocalListener(LocalListener listener) {
		if (!localListeners.contains(listener))
			localListeners.add(listener);
	}

	@Override
	public synchronized KnowledgeManager removeReplica(KnowledgeManager km) {
		if (replicas.contains(km)) {
			replicas.remove(km);
			for (ReplicaListener listener : replicaListeners)
				listener.replicaRemoved(km, this);
			return km;
		}
		return null;
	}

	@Override
	public synchronized List<KnowledgeManager> getReplicas() {
		return replicas;
	}

	@Override
	public synchronized void registerReplicaListener(ReplicaListener listener) {
		if (!replicaListeners.contains(listener))
			replicaListeners.add(listener);
	}

}
