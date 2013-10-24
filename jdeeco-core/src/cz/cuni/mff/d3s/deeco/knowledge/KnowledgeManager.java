package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

public interface KnowledgeManager {
	//XXX: MK: I changed the list to single KnowledgeReference for clarity
	//FIXME: TB: This is not a good idea, the list of references was there on purpose - because of being able to atomically retrieve a valueset
	Object get(Object knowledgeReference);
	void update(Object changeSet);
	void register(Trigger trigger);
}
