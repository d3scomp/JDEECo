package cz.cuni.mff.d3s.deeco.knowledge;


public abstract class KnowledgeChangeCollector implements ISession {
	
	private ChangeNotifier cn = null;
	
	protected void notifyAboutKnowledgeChange() {
		if (cn != null)
			cn.notifyAboutChanges(this);
	}
	
	public boolean isKnowledgeRepositoryRegistered() {
		return cn != null;
	}
	
	public void registerKnowledgeRepository(KnowledgeRepository knowledgeRepository) {
		if (cn == null) {
			cn = new ChangeNotifier(knowledgeRepository);
		}
	}
	
	public void knowledgeChanges(String knowledgePath) {
		if (cn != null)
			cn.knowledgeWritten(knowledgePath);
	}

}
