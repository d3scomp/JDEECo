package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;

public class DataclassKnowledgeContainer implements KnowledgeContainer {

	public DataclassKnowledgeContainer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public <TRole> Collection<TRole> getUntrackedKnowledgeForRole(
			Class<TRole> roleClass) throws KnowledgeContainerException {
		return Collections.emptyList();
	}

	@Override
	public <TRole> Collection<TRole> getTrackedKnowledgeForRole(
			Class<TRole> roleClass) throws KnowledgeContainerException {
		return Collections.emptyList();
	}

	@Override
	public void resetTracking() {
		// TODO Auto-generated method stub

	}

	@Override
	public void commitChanges() throws KnowledgeContainerException {
		// TODO Auto-generated method stub

	}
	
	public <TComponent>void storeComponent(TComponent component) {
		
	}

}
