package cz.cuni.mff.d3s.deeco.knowledge;

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
	 * FIXME: This method should be somewhere else.
	 * 
	 * @param longer
	 * @param shorter
	 * @return
	 */
	public static boolean contains(List<?> longer, List<?> shorter) {
		assert (longer != null && shorter != null);
		if (shorter.isEmpty())
			return true;
		if (longer.isEmpty())
			return false;
		int index = longer.indexOf(shorter.get(0));
		if (index < 0)
			return false;
		index++;
		for (int i = 1; i < shorter.size(); i++, index++) {
			if (index != longer.indexOf(shorter.get(i)))
				return false;
		}
		return true;
	}

}
