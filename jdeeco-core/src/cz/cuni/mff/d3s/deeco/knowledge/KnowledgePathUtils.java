package cz.cuni.mff.d3s.deeco.knowledge;

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
	
}
