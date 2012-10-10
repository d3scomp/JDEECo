package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.List;


public interface IKnowledgeChangeListener {
	public void knowledgeChanged(String triggerer, ETriggerType recMode);
	public List<String> getKnowledgePaths();
}
