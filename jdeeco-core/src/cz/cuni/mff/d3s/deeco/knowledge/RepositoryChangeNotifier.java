package cz.cuni.mff.d3s.deeco.knowledge;

public interface RepositoryChangeNotifier {
	void addKnowledgeChangeListener(IKnowledgeChangeListener listener);
}
