package cz.cuni.mff.d3s.deeco.knowledge.container;

import java.util.Arrays;
import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;

/**
 * 
 * @author Zbyněk Jiráček
 *
 */
public class ReadOnlyKnowledgeWrapper {

	private ReadOnlyKnowledgeManager knowledgeManager;
	
	public ReadOnlyKnowledgeWrapper(ReadOnlyKnowledgeManager km) {
		knowledgeManager = km;
	}

	public Collection<Class<?>> getRoles() {
		return Arrays.asList(knowledgeManager.getRoles());
	}
	
	public boolean hasRole(Class<?> roleClass) {
		return getRoles().contains(roleClass);
	}
	
	public <TRole> TRole getUntrackedRoleKnowledge(Class<TRole> roleClass) {
		TRole result;
		
		try {
			result = roleClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!hasRole(roleClass)) {
			// TODO throw exception
		}
		
		
		
		return result;
	}
}
