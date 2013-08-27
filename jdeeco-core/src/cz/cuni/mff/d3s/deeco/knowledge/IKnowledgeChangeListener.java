package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;


public interface IKnowledgeChangeListener {
	void knowledgeChanged(String triggerer, TriggerType recMode);
	List<String> getKnowledgePaths();
}
