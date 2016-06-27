package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;

public class DataclassKnowledgeContainer implements KnowledgeContainer {
	
	private List<Object> componentData;

	public DataclassKnowledgeContainer() {
		componentData = new ArrayList<Object>();
	}
	
	private <TRole> boolean hasRole(Object component, Class<TRole> roleClass) {
		return roleClass.isAssignableFrom(component.getClass());
	}

	@Override
	public <TRole> Collection<TRole> getUntrackedKnowledgeForRole(
			Class<TRole> roleClass) throws KnowledgeContainerException {
		
		List<TRole> result = new ArrayList<>();
		
		for (Object comp : componentData) {
			if(this.hasRole(comp, roleClass))
				result.add((TRole) comp);				
		}
		
		return result;
	}

	@Override
	public <TRole> Collection<TRole> getTrackedKnowledgeForRole(
			Class<TRole> roleClass) throws KnowledgeContainerException {
		return getUntrackedKnowledgeForRole(roleClass);
	}

	@Override
	public void resetTracking() {
		// TODO Auto-generated method stub

	}

	@Override
	public void commitChanges() throws KnowledgeContainerException {
		// TODO Auto-generated method stub

	}
	
	public <TComponent>void storeDataClass(TComponent component) {
		componentData.add(component);
	}
}
