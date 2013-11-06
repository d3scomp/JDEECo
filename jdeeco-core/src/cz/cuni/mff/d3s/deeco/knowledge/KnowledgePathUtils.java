package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class KnowledgePathUtils {

	public static KnowledgePath createKnowledgePath() {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		return factory.createKnowledgePath();
	}

	public static PathNodeField createPathNodeField() {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		return factory.createPathNodeField();
	}
	
	public static KnowledgeChangeTrigger createKnowledgeChangeTrigger() {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		return factory.createKnowledgeChangeTrigger();
	}

	/**
	 * Checks whether the shorter list is contained in the longer one, keeping the order.
	 * It returns the containment end index.
	 * FIXME: This method should be somewhere else.
	 * 
	 * @param longer
	 * @param shorter
	 * @return
	 */
	public static int containmentEndIndex(List<?> longer, List<?> shorter) {
		assert (longer != null && shorter != null);
		//We need this as EList has differs in implementation of List specification
		//See indexOf method.
		List<Object> longerList = new LinkedList<>();
		List<Object> shorterList = new LinkedList<>();
		longerList.addAll(longer);
		shorterList.addAll(shorter);
		if (shorterList.isEmpty())
			return 0;
		if (longerList.isEmpty())
			return -1;
		int index = longerList.indexOf(shorterList.get(0));
		if (index < 0)
			return -1;
		index++;
		for (int i = 1; i < shorterList.size(); i++, index++) {
			if (index != longerList.indexOf(shorterList.get(i)))
				return -1;
		}
		index--;
		return index;
	}

}
