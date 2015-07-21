package cz.cuni.mff.d3s.deeco.knowledge.container;

import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;

//cannot be inside any *Test class as it can't be instantiated there
class TestRole {
	
	public static KnowledgePath kps;
	public static KnowledgePath kpi;
	
	static {
		try {
			kps = KnowledgePathHelper.createKnowledgePath("s", PathOrigin.COMPONENT);
			kpi = KnowledgePathHelper.createKnowledgePath("i", PathOrigin.COMPONENT);
		} catch (ParseException | AnnotationProcessorException e) {
			e.printStackTrace();
		}
	}
	
	public String s;
	public Integer i;
}

class TestRole2 {
	public static KnowledgePath kpc;
	
	static {
		try {
			kpc = KnowledgePathHelper.createKnowledgePath("c", PathOrigin.COMPONENT);
		} catch (ParseException | AnnotationProcessorException e) {
			e.printStackTrace();
		}
	}
	
	public Character c;
}
