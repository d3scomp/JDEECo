package cz.cuni.mff.d3s.jdeeco.simulation.jni;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeData;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;

public class DemoKnowledgeData extends KnowledgeData {

	public int rebroadcastCount = 0;
	
	public DemoKnowledgeData(String componentId, ValueSet knowledge) {
		super(componentId, knowledge);
	}
	
}
