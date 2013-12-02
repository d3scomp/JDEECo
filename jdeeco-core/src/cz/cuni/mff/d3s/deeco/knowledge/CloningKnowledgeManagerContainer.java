package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * This container allows to deal with both local and replica knowledge managers by implementing both interfaces of {@link LocalKnowledgeManagerContainer} 
 * and  {@link ReplicaKnowledgeManagerContainer}. 
 * It acts as a factory for locals and replicas and gives the ability to register a local listener and a replica listener for the knowledge manager. Also, 
 * it retrieves the available locals and replicas in a container.   
 * 
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
		for (LocalListener listener : localListeners){
			listener.localCreated(result, this);
		}
		return result;
	}

	@Override
	public KnowledgeManager removeLocal(KnowledgeManager km) {
		KnowledgeManager kmVar = null;
		if (locals.contains(km)) {
			locals.remove(km);
			for (LocalListener listener : localListeners){
				listener.localRemoved(km, this);
			}
			kmVar = km;
		}
		return kmVar;
	}

	@Override
	public List<KnowledgeManager> getLocals() {
		return locals;
	}

	@Override
	public void registerLocalListener(LocalListener listener) {
		if (!localListeners.contains(listener)){
			localListeners.add(listener);
		}
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
