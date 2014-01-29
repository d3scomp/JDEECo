package cz.cuni.mff.d3s.jdeeco.simulation.demo;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeData;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeDataReceiver;


public class TestKnowledgeDataReceiver implements KnowledgeDataReceiver {

	@Override
	public void receive(List<KnowledgeData> knowledgeData) {
		for (KnowledgeData kd : knowledgeData)
			System.out.println("Received knowledge from " + kd.getOwnerId());		
	}

}
