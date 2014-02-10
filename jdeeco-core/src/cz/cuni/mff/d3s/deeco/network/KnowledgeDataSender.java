package cz.cuni.mff.d3s.deeco.network;

import java.util.List;

/**
 * An object that is capable of sending {@link KnowledgeData} on the network.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public interface KnowledgeDataSender {
	
	public void broadcastKnowledgeData(List<? extends KnowledgeData> knowledgeData);
	
	//TODO We don't need it now
	//public void sendKnowledgeData(KnowledgeData knowledgeData, String recipient);
}
