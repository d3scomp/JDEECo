package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

public class CloningKnowledgeManagerFactory implements KnowledgeManagerFactory {

	@Override
	public KnowledgeManager create(String id, ComponentInstance component, Class<?>[] roleClasses) {
		return new CloningKnowledgeManager(id, component, roleClasses);
	}

}
