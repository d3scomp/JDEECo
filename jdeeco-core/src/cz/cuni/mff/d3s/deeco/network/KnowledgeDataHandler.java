package cz.cuni.mff.d3s.deeco.network;

public interface KnowledgeDataHandler {
	public KnowledgeDataSender getKnowledgeDataSender();
	public void setKnowledgeDataReceiver(KnowledgeDataReceiver knowledgeDataReceiver);
}
