package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

public class KnowledgeManagerViewImpl implements KnowledgeManagersView,
		ReplicaListener, LocalListener {

	@Override
	public void localCreated(KnowledgeManager km) {
		// TODO Auto-generated method stub

	}

	@Override
	public void localRemoved(KnowledgeManager km) {
		// TODO Auto-generated method stub

	}

	@Override
	public void replicaCreated(KnowledgeManager km) {
		// TODO Auto-generated method stub

	}

	@Override
	public void replicaRemoved(KnowledgeManager km) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<ReadOnlyKnowledgeManager> getOthersKnowledgeManagers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(Trigger trigger, ShadowsTriggerListener triggerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregister(Trigger trigger,
			ShadowsTriggerListener triggerListener) {
		// TODO Auto-generated method stub

	}

}
