package cz.cuni.mff.d3s.jdeeco.simulation.jni;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;

public class DemoKnowledgeData extends KnowledgeData {

	public int rebroadcastCount = 0;
	
	public DemoKnowledgeData(String componentId, ValueSet knowledge, long version, String sender) {
		super(componentId, knowledge, version, sender);
	}
	
}
