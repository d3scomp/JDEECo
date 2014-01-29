package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

public interface KnowledgeDataSender {
	
	public void broadcastKnowledgeData(List<KnowledgeData> knowledgeData);
	
	//TODO We don't need it now
	//public void sendKnowledgeData(KnowledgeData knowledgeData, String recipient);
}
