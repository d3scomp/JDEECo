package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

public class KnowledgePathBuilder {
	private static RuntimeMetadataFactory factory = RuntimeMetadataFactoryExt.eINSTANCE;
	
	public static KnowledgePath buildSimplePath(String pathString) {
		KnowledgePath kp = factory.createKnowledgePath();
		
		for (String part: pathString.split("\\.")) {
			PathNodeField pn = factory.createPathNodeField();
			pn.setName(part);
			kp.getNodes().add(pn);
		}
		
		return kp;
	}
}
