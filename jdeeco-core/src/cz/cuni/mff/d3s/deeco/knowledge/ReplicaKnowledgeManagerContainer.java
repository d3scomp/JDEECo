package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface ReplicaKnowledgeManagerContainer {
	public KnowledgeManager createReplicaFor(KnowledgeManager km);
	public KnowledgeManager removeReplica(KnowledgeManager km);
	public List<KnowledgeManager> getReplicas();
	public void registerReplicaListener(ReplicaListener listener);
}
