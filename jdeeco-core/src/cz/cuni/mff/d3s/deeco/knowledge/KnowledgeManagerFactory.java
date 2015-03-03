package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

public interface KnowledgeManagerFactory {

	public KnowledgeManager create(String id, ComponentInstance component, Class<?>[] roleClasses);
	
}
