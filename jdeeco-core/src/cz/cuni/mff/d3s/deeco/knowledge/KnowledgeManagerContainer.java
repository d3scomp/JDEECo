package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class KnowledgeManagerContainer implements
		ReplicaKnowledgeManagerContainer, LocalKnowledgeManagerContainer {

	@Override
	public KnowledgeManager createLocal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KnowledgeManager removeLocal(KnowledgeManager km) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KnowledgeManager> getLocals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerListener(LocalListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public KnowledgeManager createReplicaFor(KnowledgeManager km) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KnowledgeManager removeReplicaFor(KnowledgeManager km) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KnowledgeManager> getReplicas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerListener(ReplicaListener listener) {
		// TODO Auto-generated method stub

	}

}
