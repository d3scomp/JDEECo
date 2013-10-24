package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

public interface KnowledgeManager {
	//I changed the list to single KnowledgeReference for clarity - mkit
	Object get(Object knowledgeReference);
	void update(Object changeSet);
	void register(Trigger trigger);
}
