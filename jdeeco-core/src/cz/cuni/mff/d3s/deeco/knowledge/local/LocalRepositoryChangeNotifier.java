package cz.cuni.mff.d3s.deeco.knowledge.local;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.IKnowledgeChangeListener;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryChangeNotifier;

public class LocalRepositoryChangeNotifier implements RepositoryChangeNotifier {
	private List<IKnowledgeChangeListener> knowledgeListeners;
	private String lastProcessed = null;
	private KnowledgeRepository kr;

	public LocalRepositoryChangeNotifier(KnowledgeRepository kr) {
		this.knowledgeListeners = new LinkedList<>();
		this.kr = kr;
	}

	@Override
	public void addKnowledgeChangeListener(IKnowledgeChangeListener listener) {
		if (!knowledgeListeners.contains(listener))
			knowledgeListeners.add(listener);
	}

	public void notify(String knowledgePath) {
		if (kr.isTriggeringActive()) {
			lastProcessed = kr.notify(knowledgePath, lastProcessed,
					knowledgeListeners);
		}
	}
}
