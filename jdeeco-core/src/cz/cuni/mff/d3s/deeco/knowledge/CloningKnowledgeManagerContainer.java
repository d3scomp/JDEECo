package cz.cuni.mff.d3s.deeco.knowledge;

import com.rits.cloning.Cloner;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class CloningKnowledgeManagerContainer extends KnowledgeManagerContainer {

	private final Cloner cloner;

	public CloningKnowledgeManagerContainer() {
		this.cloner = new Cloner();
	}

	@Override
	public synchronized KnowledgeManager createReplicaFor(KnowledgeManager km) {
		if (!replicas.contains(km)) {
			KnowledgeManager result = cloner.deepClone(km);
			replicas.add(result);
			for (ReplicaListener listener : replicaListeners)
				listener.replicaCreated(result);
			return result;
		}
		return null;
	}

}
