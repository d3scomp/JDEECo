package cz.cuni.mff.d3s.deeco.knowledge;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface ReplicaListener {
	public void replicaCreated(KnowledgeManager km, ReplicaKnowledgeManagerContainer container);
	public void replicaRemoved(KnowledgeManager km, ReplicaKnowledgeManagerContainer container);
}