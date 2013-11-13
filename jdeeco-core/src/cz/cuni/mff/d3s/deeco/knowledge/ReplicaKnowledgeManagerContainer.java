package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface ReplicaKnowledgeManagerContainer {
	// FIXME TB: I can imagine that instead of createReplicaFor you will need only createReplica
	// to return you an empty knowledge manager, which you then populate using the synchronizer
	// I think that especially in the real distributed case when you will be getting only partial
	// data this will be more appropriate.
	// However, for now, it may stay, as this functionality is anyway not needed for milestone #1
	public KnowledgeManager createReplicaFor(KnowledgeManager km);
	public KnowledgeManager removeReplica(KnowledgeManager km);
	public List<KnowledgeManager> getReplicas();
	public void registerReplicaListener(ReplicaListener listener);
}
