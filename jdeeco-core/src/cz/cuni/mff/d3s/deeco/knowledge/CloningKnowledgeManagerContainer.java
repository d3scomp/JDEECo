package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class CloningKnowledgeManagerContainer implements
		ReplicaKnowledgeManagerContainer, LocalKnowledgeManagerContainer {

	protected final List<KnowledgeManager> locals;
	protected final List<KnowledgeManager> replicas;
	protected final List<LocalListener> localListeners;
	protected final List<ReplicaListener> replicaListeners;

	public CloningKnowledgeManagerContainer() {
		this.locals = new LinkedList<>();
		this.replicas = new LinkedList<>();
		this.localListeners = new LinkedList<>();
		this.replicaListeners = new LinkedList<>();
	}

	@Override
	public KnowledgeManager createLocal(String id) {
		KnowledgeManager result = new CloningKnowledgeManager(id);
		locals.add(result);
		for (LocalListener listener : localListeners)
			listener.localCreated(result, this);
		return result;
	}

	@Override
	public KnowledgeManager removeLocal(KnowledgeManager km) {
		if (locals.contains(km)) {
			locals.remove(km);
			for (LocalListener listener : localListeners)
				listener.localRemoved(km, this);
			return km;
		}
		return null;
	}

	@Override
	public List<KnowledgeManager> getLocals() {
		return locals;
	}

	@Override
	public void registerLocalListener(LocalListener listener) {
		if (!localListeners.contains(listener))
			localListeners.add(listener);
	}

	@Override
	public KnowledgeManager removeReplica(KnowledgeManager km) {
		if (replicas.contains(km)) {
			replicas.remove(km);
			for (ReplicaListener listener : replicaListeners)
				listener.replicaRemoved(km, this);
			return km;
		}
		return null;
	}

	@Override
	public List<KnowledgeManager> getReplicas() {
		return replicas;
	}

	@Override
	public void registerReplicaListener(ReplicaListener listener) {
		if (!replicaListeners.contains(listener))
			replicaListeners.add(listener);
	}

	@Override
	public KnowledgeManager createReplica(String id) {
		KnowledgeManager result = new CloningKnowledgeManager(id);
		if (!(locals.contains(result) || replicas.contains(result))) {
			replicas.add(result);
			for (ReplicaListener listener : replicaListeners)
				listener.replicaCreated(result, this);
			return result;
		} else
			return null;
	}

}
