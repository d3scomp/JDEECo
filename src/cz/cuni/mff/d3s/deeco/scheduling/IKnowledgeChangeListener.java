package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.List;


public interface IKnowledgeChangeListener {
	public void knowledgeChanged();
	public List<String> getKnowledgePaths();
}
