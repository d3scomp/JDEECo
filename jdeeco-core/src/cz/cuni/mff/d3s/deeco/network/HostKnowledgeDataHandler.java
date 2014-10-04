package cz.cuni.mff.d3s.deeco.network;

public interface HostKnowledgeDataHandler {
	public KnowledgeDataSender getKnowledgeDataSender();
	public void setKnowledgeDataReceiver(KnowledgeDataReceiver knowledgeDataReceiver);
}
