package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

public class ReadOnlyKnowledgeManagerImpl implements ReadOnlyKnowledgeManager{

	@Override
	public ValueSet get(Collection<KnowledgeReference> knowledgeReferenceList) {
		return null;
	}

	@Override
	public void register(Trigger trigger, TriggerListenerTest triggerListener) {
	}

}
