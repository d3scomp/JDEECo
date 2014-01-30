package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

public interface KnowledgeDataReceiver {
	public void receive(List<? extends KnowledgeData> knowledgeData);
}
